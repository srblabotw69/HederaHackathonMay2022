// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.13;

import "/openzeppelin-solidity/contracts/token/ERC20/ERC20.sol";

/**
 * @title ERC1400 security token standard
 * @dev ERC1400 logic
 */
interface IERC1400  {
    
    function getDocument(bytes32 name) external view returns ( string memory, bytes32 );
  	function setDocument(bytes32 name, string calldata uri, bytes32 documentHash) external;
  	
}