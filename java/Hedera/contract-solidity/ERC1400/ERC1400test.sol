// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <=0.8.13;

contract ERC1400test {
    // the contract's owner, set in the constructor
    address owner;

    // the message we're storing
    string message;

    constructor(string memory document_) {
        // set the owner of the contract for `kill()`
        owner = msg.sender;
        document = document_;
    }

    function set_document(string memory document_) public {
        // only allow the owner to update the message
        if (msg.sender != owner) return;
        document = message_;
    }

    // return a string
    function get_document() public view returns (string document) {
        return document;
    }
}