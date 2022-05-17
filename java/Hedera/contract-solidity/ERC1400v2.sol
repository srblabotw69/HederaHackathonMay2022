// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <=0.8.13;

import "./IERC1400.sol";

contract ERC1400v2 is IERC1400 {

  bytes32 name;
  string uri;
  bytes32 documentHash;
  bytes32[] documentHashArray;
 
  struct DocStruct { 
     bytes32 name;
     string uri;
	 bytes32 docHash;
   }
  
  constructor() {
 
  }
    
  function getDocument(bytes32 _name) external view returns (string memory, bytes32)    {
    bytes32[] memory temp;
    temp[0] = _name;
   	return (uri, documentHash);
  }
    
  function setDocument(bytes32 _name, string calldata _uri, bytes32 _documentHash) external {
    documentHash = _documentHash;
  	name = _name;
  	uri = _uri;
  	uri = "test_url_test";
  }
     
  function getAllDocuments() external view returns (bytes32[] memory) {
  	return (documentHashArray);
  }
}