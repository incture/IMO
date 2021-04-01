//
// CollectionsViewController.swift
// MurphyDFT-Final
//
// Created by SAP Cloud Platform SDK for iOS Assistant application on 01/02/18
//

import Foundation
import SAPFiori

protocol TableUpdaterDelegate {
    func updateTable()
}

class CollectionsViewController: FUIFormTableViewController {

    private var collections = CollectionType.all

    // Variable to store the selected index path
    private var selectedIndex: IndexPath?

    private let appDelegate = UIApplication.shared.delegate as! AppDelegate

    var isPresentedInSplitView: Bool {
        return !(self.splitViewController?.isCollapsed ?? true)
    }

    // MARK: - Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        self.preferredContentSize = CGSize(width: 320, height: 480)

        self.tableView.rowHeight = UITableView.automaticDimension
        self.tableView.estimatedRowHeight = 44
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.makeSelection()
    }

    override func viewWillTransition(to _: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        coordinator.animate(alongsideTransition: nil, completion: { _ in
            let isNotInSplitView = !self.isPresentedInSplitView
            self.tableView.visibleCells.forEach { cell in
                // To refresh the disclosure indicator of each cell
                cell.accessoryType = isNotInSplitView ? .disclosureIndicator : .none
            }
            self.makeSelection()
        })
    }

    // MARK: - UITableViewDelegate
    override func numberOfSections(in _: UITableView) -> Int {
        return 1
    }

    override func tableView(_: UITableView, numberOfRowsInSection _: Int) -> Int {
        return self.collections.count
    }

    override func tableView(_: UITableView, heightForRowAt _: IndexPath) -> CGFloat {
        return 44
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: FUIObjectTableViewCell.reuseIdentifier, for: indexPath) as! FUIObjectTableViewCell
        cell.headlineLabel.text = self.collections[indexPath.row].rawValue
        cell.accessoryType = !self.isPresentedInSplitView ? .disclosureIndicator : .none
        cell.isMomentarySelection = false
        return cell
    }
//    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> FUIObjectTableViewCell {
//        
//    }

    override func tableView(_: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.selectedIndex = indexPath
        self.collectionSelected(at: indexPath)
    }

    // CollectionType selection helper

    private func collectionSelected(at: IndexPath) {
        // Load the EntityType specific ViewController from the specific storyboard
        var masterViewController: UIViewController!
        switch self.collections[at.row] {
        case .createSet:
            let createSetStoryBoard = UIStoryboard(name: "CreateSet", bundle: nil)
            masterViewController = createSetStoryBoard.instantiateViewController(withIdentifier: "CreateSetMaster")
            masterViewController.navigationItem.title = "CreateSet"
        case .changeSet:
            let changeSetStoryBoard = UIStoryboard(name: "ChangeSet", bundle: nil)
            masterViewController = changeSetStoryBoard.instantiateViewController(withIdentifier: "ChangeSetMaster")
            masterViewController.navigationItem.title = "ChangeSet"
        case .getPurGrpSet:
            let getPurGrpSetStoryBoard = UIStoryboard(name: "GetPurGrpSet", bundle: nil)
            masterViewController = getPurGrpSetStoryBoard.instantiateViewController(withIdentifier: "GetPurGrpSetMaster")
            masterViewController.navigationItem.title = "GetPurGrpSet"
        case .rejectSet:
            let rejectSetStoryBoard = UIStoryboard(name: "RejectSet", bundle: nil)
            masterViewController = rejectSetStoryBoard.instantiateViewController(withIdentifier: "RejectSetMaster")
            masterViewController.navigationItem.title = "RejectSet"
        case .requestSet:
            let requestSetStoryBoard = UIStoryboard(name: "RequestSet", bundle: nil)
            masterViewController = requestSetStoryBoard.instantiateViewController(withIdentifier: "RequestSetMaster")
            masterViewController.navigationItem.title = "RequestSet"
        case .getPurGrpByUserNameSet:
            let getPurGrpByUserNameSetStoryBoard = UIStoryboard(name: "GetPurGrpByUserNameSet", bundle: nil)
            masterViewController = getPurGrpByUserNameSetStoryBoard.instantiateViewController(withIdentifier: "GetPurGrpByUserNameSetMaster")
            masterViewController.navigationItem.title = "GetPurGrpByUserNameSet"
        case .getPurGrpRequestDetailsSet:
            let getPurGrpRequestDetailsSetStoryBoard = UIStoryboard(name: "GetPurGrpRequestDetailsSet", bundle: nil)
            masterViewController = getPurGrpRequestDetailsSetStoryBoard.instantiateViewController(withIdentifier: "GetPurGrpRequestDetailsSetMaster")
            masterViewController.navigationItem.title = "GetPurGrpRequestDetailsSet"
        case .getResponsiblePersonSet:
            let getResponsiblePersonSetStoryBoard = UIStoryboard(name: "GetResponsiblePersonSet", bundle: nil)
            masterViewController = getResponsiblePersonSetStoryBoard.instantiateViewController(withIdentifier: "GetResponsiblePersonSetMaster")
            masterViewController.navigationItem.title = "GetResponsiblePersonSet"
        case .getNxtPurGrpByCtrySet:
            let getNxtPurGrpByCtrySetStoryBoard = UIStoryboard(name: "GetNxtPurGrpByCtrySet", bundle: nil)
            masterViewController = getNxtPurGrpByCtrySetStoryBoard.instantiateViewController(withIdentifier: "GetNxtPurGrpByCtrySetMaster")
            masterViewController.navigationItem.title = "GetNxtPurGrpByCtrySet"
        case .none:
            masterViewController = UIViewController()
        }

        // Load the NavigationController and present with the EntityType specific ViewController
        let mainStoryBoard = UIStoryboard(name: "Main", bundle: nil)
        let rightNavigationController = mainStoryBoard.instantiateViewController(withIdentifier: "RightNavigationController") as! UINavigationController
        rightNavigationController.viewControllers = [masterViewController]
        self.splitViewController?.showDetailViewController(rightNavigationController, sender: nil)
    }

    // MARK: - Handle highlighting of selected cell
    private func makeSelection() {
        if let selectedIndex = selectedIndex {
            self.tableView.selectRow(at: selectedIndex, animated: true, scrollPosition: .none)
            self.tableView.scrollToRow(at: selectedIndex, at: .none, animated: true)
        } else {
            self.selectDefault()
        }
    }

    private func selectDefault() {
        // Automatically select first element if we have two panels (iPhone plus and iPad only)
        if self.splitViewController!.isCollapsed || self.appDelegate.zmurpurchasegroupsrvEntities == nil {
            return
        }
        let indexPath = IndexPath(row: 0, section: 0)
        self.tableView.selectRow(at: indexPath, animated: true, scrollPosition: .middle)
        self.collectionSelected(at: indexPath)
    }
}
