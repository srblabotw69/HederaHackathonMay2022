// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <=0.8.13;


import "./IERC1400.sol";
 
 
contract ERC1400 is IERC1400 {

	// Private variables
    bytes32 private docHash;
    
    function getDocument(bytes32 name) external view returns (string memory, bytes32) {
    	return ("getDocument", name);
    }
    
  	function setDocument(bytes32 name, string calldata uri, bytes32 documentHash) external {
  		docHash = documentHash;
  	}
}
