// SPDX-License-Identifier: GPL-3.0
pragma solidity 0.8.13;

import "../openzeppelin-solidity/contracts/token/ERC20/ERC20.sol"; 

contract GLDToken is ERC20 {
    constructor(uint256 initialSupply) ERC20("Gold", "GLD") {
        _mint(msg.sender, initialSupply);
    }
}