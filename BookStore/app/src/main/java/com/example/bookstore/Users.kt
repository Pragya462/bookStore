package com.example.bookstore

class Users {

    var profilepic :String? = null
    var name :String? = null
    var email :String? = null
    var password :String? = null
    var number: String? = null
    var uid: String? = null

    constructor(){

    }

    constructor(name: String?, email: String?, password: String?, number: String?)
    {
        this.name = name
        this.email = email
        this.password = password
        this.number = number
    }
}