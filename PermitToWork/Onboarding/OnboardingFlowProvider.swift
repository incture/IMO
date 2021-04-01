//
// DigitialApps
//
// Created by SAP Cloud Platform SDK for iOS Assistant application on 26/11/19
//

import SAPCommon
import SAPFiori
import SAPFioriFlows
import SAPFoundation
import WebKit

public class OnboardingFlowProvider: OnboardingFlowProviding {
    // MARK: – Properties

    public static let modalUIViewControllerPresenter = ModalUIViewControllerPresenter()

    // MARK: – Init

    public init() {}

    // MARK: – OnboardingFlowProvider

    public func flow(for _: OnboardingControlling, flowType: OnboardingFlow.FlowType, completionHandler: @escaping (OnboardingFlow?, Error?) -> Void) {
        switch flowType {
        case .onboard:
            completionHandler(self.onboardingFlow(), nil)
        case let .restore(onboardingID):
            completionHandler(self.restoringFlow(for: onboardingID), nil)
        case let .reset(onboardingID):
            completionHandler(self.resettingFlow(for: onboardingID), nil)
        @unknown default:
            break
        }
    }

    // MARK: – Internal

    func onboardingFlow() -> OnboardingFlow {
        let steps = self.onboardingSteps
        let context = OnboardingContext(presentationDelegate: OnboardingFlowProvider.modalUIViewControllerPresenter)
        let flow = OnboardingFlow(flowType: .onboard, context: context, steps: steps)
        ImoPtwNetworkManager.shared.urlSession = context.sapURLSession
        AppDelegate.shared.sessionManagerPMapp = context
        let defaults = UserDefaults.standard
        defaults.set("onboardtrue", forKey: "onboardingTrue")
        return flow
    }

    func restoringFlow(for onboardingID: UUID) -> OnboardingFlow {
      
        let steps = self.restoringSteps
        var context = OnboardingContext(onboardingID: onboardingID, presentationDelegate: OnboardingFlowProvider.modalUIViewControllerPresenter)
        context.onboardingID = onboardingID
        let flow = OnboardingFlow(flowType: .restore(onboardingID: onboardingID), context: context, steps: steps)
        ImoPtwNetworkManager.shared.urlSession = context.sapURLSession
        AppDelegate.shared.sessionManagerPMapp = context
        let defaults = UserDefaults.standard
        defaults.set("onboardtrue", forKey: "onboardingTrue")
        return flow
    }

    func resettingFlow(for onboardingID: UUID) -> OnboardingFlow {
        let steps = self.resettingSteps
        var context = OnboardingContext(onboardingID: onboardingID, presentationDelegate: OnboardingFlowProvider.modalUIViewControllerPresenter)
        context.onboardingID = onboardingID
        let flow = OnboardingFlow(flowType: .reset(onboardingID: onboardingID), context: context, steps: steps)
        ImoPtwNetworkManager.shared.urlSession = context.sapURLSession
        AppDelegate.shared.sessionManagerPMapp = context
        UserDefaults.standard.removeObject(forKey: "onboardingTrue")
        return flow
    }

    // MARK: - Steps

    public var onboardingSteps: [OnboardingStep] {
        return [
            self.configuredWelcomeScreenStep(),
            CompositeStep(steps: SAPcpmsDefaultSteps.configuration),
            SAMLAuthenticationStep(),
            CompositeStep(steps: SAPcpmsDefaultSteps.settingsDownload),
            CompositeStep(steps: SAPcpmsDefaultSteps.applyDuringOnboard),
//            self.configuredUserConsentStep(),
            self.configuredUsageCollectionConsentStep(),
            self.configuredStoreManagerStep(),
        ]
    }

    
    public var restoringSteps: [OnboardingStep] {
        return [
            self.configuredStoreManagerStep(),
            self.configuredWelcomeScreenStep(),
            CompositeStep(steps: SAPcpmsDefaultSteps.configuration),
            SAMLAuthenticationStep(),
            CompositeStep(steps: SAPcpmsDefaultSteps.settingsDownload),
            CompositeStep(steps: SAPcpmsDefaultSteps.applyDuringRestore.map { ($0 as? PasscodePolicyApplyStep)?.defaultPasscodePolicy = nil; return $0 }),
            self.configuredUsageCollectionConsentStep(),
        ]
    }

    public var offlineSyncingSteps: [OnboardingStep] {
        return [
            self.configuredWelcomeScreenStep(),
            CompositeStep(steps: SAPcpmsDefaultSteps.settingsDownload),
            CompositeStep(steps: SAPcpmsDefaultSteps.applyDuringRestore.map { ($0 as? PasscodePolicyApplyStep)?.defaultPasscodePolicy = nil; return $0 }),
        ]
    }

    public var resettingSteps: [OnboardingStep] {
        return self.onboardingSteps
    }

    // MARK: – Step configuration

    private func configuredWelcomeScreenStep() -> WelcomeScreenStep {
        let discoveryConfigurationTransformer = DiscoveryServiceConfigurationTransformer(applicationID: "com.incture.taskmanagement", authenticationPath: "com.staticmetadata.dest")
        let welcomeScreenStep = WelcomeScreenStep(transformer: discoveryConfigurationTransformer, providers:  [FileConfigurationProvider()])

        welcomeScreenStep.welcomeScreenCustomizationHandler = { welcomeScreen in
            welcomeScreen.headlineLabel.text = "IMO-PTW"
            welcomeScreen.detailLabel.text = ""
            welcomeScreen.primaryActionButton.titleLabel?.text = NSLocalizedString("keyWelcomeScreenStartButton", value: "Login", comment: "XBUT: Title of start button on WelcomeScren")
            
            welcomeScreen.isDemoModeAvailable = false
            welcomeScreen.primaryActionButton.tintColor = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)
        }

        return welcomeScreenStep
    }

    private func configuredUserConsentStep() -> UserConsentStep {
        let actionTitle = "Learn more about Data Privacy"
        let actionUrl = "https://www.sap.com/corporate/en/legal/privacy.html"
        let singlePageTitle = "Data Privacy"
        let singlePageText = "Detailed text about how data privacy pertains to this app and why it is important for the user to enable this functionality"

        var singlePageContent = UserConsentPageContent()
        singlePageContent.actionTitle = actionTitle
        singlePageContent.actionUrl = actionUrl
        singlePageContent.title = singlePageTitle
        singlePageContent.body = singlePageText
        let singlePageFormContent = UserConsentFormContent(version: "1.0", isRequired: true, pages: [singlePageContent])

        return UserConsentStep(userConsentFormsContent: [singlePageFormContent])
    }

    private func configuredUsageCollectionConsentStep() -> UsageCollectionConsentStep {
        return UsageCollectionConsentStep()
    }

    private func configuredStoreManagerStep() -> StoreManagerStep {
        let step = StoreManagerStep()
        step.defaultPasscodePolicy = nil

        return step
    }
}

// MARK: - SAPWKNavigationDelegate

// The WKWebView occasionally returns an NSURLErrorCancelled error if a redirect happens too fast.
// In case of OAuth with SAP's identity provider (IDP) we do not treat this as an error.
extension OnboardingFlowProvider: SAPWKNavigationDelegate {
    public func webView(_: WKWebView, handleFailed _: WKNavigation!, withError error: Error) -> Error? {
        if self.isCancelledError(error) {
            return nil
        }
        return error
    }

    public func webView(_: WKWebView, handleFailedProvisionalNavigation _: WKNavigation!, withError error: Error) -> Error? {
        if self.isCancelledError(error) {
            return nil
        }
        return error
    }

    private func isCancelledError(_ error: Error) -> Bool {
        let nsError = error as NSError
        return nsError.domain == NSURLErrorDomain &&
            nsError.code == NSURLErrorCancelled
    }
}
extension URLResponse {
    
    var httpStatusCode: Int? {
        get {
            guard let httpResponse = self as? HTTPURLResponse else {
                return nil
            }
            return httpResponse.statusCode
        }
    }
    
    var isSuccess: Bool {
        get {
            guard let value = self.httpStatusCode else {
                return false
            }
            return value >= 200 && value < 300
        }
    }
    
}
