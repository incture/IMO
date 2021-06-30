//
//  ICFlowLayout.swift
//  TokenView
//
//  Created by Mahabaleshwar Hegde on 20/04/18.
// 
//

import UIKit

class ICFlowLayout: UICollectionViewFlowLayout {
    
    
    
//    override func layoutAttributesForItem(at indexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
//        let attributes = super.layoutAttributesForItem(at: indexPath as IndexPath)?.copy() as? UICollectionViewLayoutAttributes
//        guard let collectionView = collectionView else { return attributes }
//        attributes?.bounds.size.width = collectionView.bounds.width - sectionInset.left - sectionInset.right
//        return attributes
//    }
    
    
    override func layoutAttributesForElements(in rect: CGRect) -> [UICollectionViewLayoutAttributes]? {
        let attributes = super.layoutAttributesForElements(in: rect)

        var leftMargin = sectionInset.left
        var maxY: CGFloat = -1.0
        attributes?.forEach { layoutAttribute in
            if layoutAttribute.frame.origin.y >= maxY {
                leftMargin = sectionInset.left
            }

            layoutAttribute.frame.origin.x = leftMargin

            leftMargin += layoutAttribute.frame.width + minimumInteritemSpacing
            maxY = max(layoutAttribute.frame.maxY , maxY)
        }

        return attributes
    }

}
