// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <=0.8.13;

contract ERC1400 {

  	bytes32 name;
  	string uri;
  	bytes32 documentHash;
    
  	function getDocument(bytes32 _name) external view returns (string memory, bytes32)    {
  		return (uri, _name);
    }
    
  	function setDocument(bytes32 _name, string calldata _uri, bytes32 _documentHash) external {
	  	name = _name;
	  	uri = _uri;
	  	documentHash = _documentHash;
  	}
}
