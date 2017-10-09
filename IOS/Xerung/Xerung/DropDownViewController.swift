//
//  DropDownViewController.swift
//  Xerung
//
//  Created by mityung on 14/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

protocol DropDownViewControllerDelegate {
    func saveString( _ strText : NSString)
}


class DropDownViewController: UIViewController , UITableViewDelegate , UITableViewDataSource  {
    var data = [String]()
    var filterData = [String]()
    @IBOutlet weak var tableView: UITableView!
   
    
   
    var delegate : DropDownViewControllerDelegate?
    let searchController = UISearchController(searchResultsController: nil)
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
      
        self.tableView.estimatedRowHeight = 100;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        
        searchController.searchResultsUpdater = self
        searchController.searchBar.placeholder = "Search here..."
        searchController.dimsBackgroundDuringPresentation = false
        searchController.searchBar.delegate = self
        
        
       
    
       // searchController.hidesNavigationBarDuringPresentation
        
        tableView.tableHeaderView = searchController.searchBar
       // navigationItem.titleView = searchController.searchBar
        
        
      
        
        
    }

    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        self.navigationController?.navigationBar.isHidden = true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if searchController.isActive && searchController.searchBar.text != "" {
            return filterData.count
        }
        
        return data.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell()
        
        if searchController.isActive && searchController.searchBar.text != "" {
            cell.textLabel?.text = filterData[indexPath.row]
        } else {
             cell.textLabel?.text = data[indexPath.row]
        }
        
       
        cell.textLabel?.numberOfLines = 0
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if((self.delegate) != nil)
        {
            
            if searchController.isActive && searchController.searchBar.text != "" {
                delegate?.saveString(filterData[indexPath.row] as NSString);
                searchController.dismiss(animated: true, completion: nil)
                 self.dismiss(animated: true, completion: nil)
            } else {
                delegate?.saveString(data[indexPath.row] as NSString);
                 self.dismiss(animated: true, completion: nil)
            }
            
           
        }
    }
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    func filterContentForSearchText(_ searchText: String, scope: String = "All") {
        filterData = data.filter { data in
            return data.lowercased().contains(searchText.lowercased())
           // return candy.name.lowercaseString.containsString(searchText.lowercaseString)
        }
        
        tableView.reloadData()
    }
    

    
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

extension DropDownViewController: UISearchBarDelegate {
    // MARK: - UISearchBar Delegate
    func searchBar(_ searchBar: UISearchBar, selectedScopeButtonIndexDidChange selectedScope: Int) {
        filterContentForSearchText(searchBar.text!, scope: searchBar.scopeButtonTitles![selectedScope])
    }
}

extension DropDownViewController: UISearchResultsUpdating {
    func updateSearchResults(for searchController: UISearchController) {
        filterContentForSearchText(searchController.searchBar.text!)
    }
}
