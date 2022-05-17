// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <=0.8.13;

import "./IERC1400.sol";

contract ERC1400v2 is IERC1400 {

  // Document Management
  function getDocument(bytes32 _name) external view returns (string memory, bytes32  ) {
 
    }
    
  function setDocument(bytes32 _name, string calldata _uri, bytes32 _documentHash) external {
   
  }
  
  
  // Token Information
  function balanceOfByPartition(bytes32 _partition, address _tokenHolder) external view returns (uint256) {
  }
  
  function partitionsOf(address _tokenHolder) external view returns (bytes32[] memory) {
  }

 
}