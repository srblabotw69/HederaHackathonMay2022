// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <=0.8.13;

import "./IERC1400.sol";

contract ERC1400v2 is IERC1400 {
 
  uint[] docBalances; // State variable
  
  bytes32 name;
  string uri;
  bytes32 documentHash;
  
  
  uint size = 0;
  string[] uriStorage = new string[](size);
  string[] documentHashStorage = new string[](size);
 
   struct docStruct { 
     bytes32 name;
     string uri;
	 bytes32 docHash;
   }

  mapping(int32 => struct) public docMap; // .push as you go
  mapping(bytes32 => bytes32[]) public documentHashMap; // .push as you go
  mapping(bytes32 => string[]) public uriMap; // .push as you go
  
  
  function getDocument(bytes32 _name) external view returns (string memory, bytes32)    {
  	 string memory _uri = documentHashStorage[_name];
  	 return (_uri, _documentHash);                
  }
    
  function setDocument(bytes32 _name, string calldata _uri, bytes32 _documentHash) external {
  
     uriStorage.push(_uri);
     documentHashStorage.push(_documentHash);
     
	 docStruct(_name, _uri, _documentHash);

 	 uriMap[_name] = documentHashStorage;     
     documentHashMap[_name] = uriStorage;
     
    //docBalances[address(this)] += 1;
  }
  
  function removeDocument(bytes32 _name) external {
    //docBalances[address(this)] -= 1;
  }
      
  //function getAllDocuments() external view returns (bytes32[] calldata) {
  //  	bytes32[] memory _success = "success";
  //	return (_success);
  //}
  
  // Token Information
  function balanceOfByPartition(bytes32 _partition, address _tokenHolder) external view returns (uint256) {
  }
  
  function partitionsOf(address _tokenHolder) external view returns (bytes32[] memory) {
  }
  
  
}