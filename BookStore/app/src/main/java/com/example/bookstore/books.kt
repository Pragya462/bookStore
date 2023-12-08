package com.example.bookstore

class books {

    var bid: String? = null
    var image: String? = null
    var name :String? = null
    var price :String? = null
    var description :String? = null
    var username: String? = null
    var userNumber: String? = null
    var uid: String? = null
    var status: String? = null

    constructor(){

    }

    constructor(bid: String?, image: String?, name: String?, price :String?, description :String?, username: String?, userNumber: String?, uid: String?, status: String)
    {
        this.bid = bid
        this.image = image
        this.name = name
        this.price = price
        this.description = description
        this.username = username
        this.userNumber = userNumber
        this.uid = uid
        this.status = status
    }
}