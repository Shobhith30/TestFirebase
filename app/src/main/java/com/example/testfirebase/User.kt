package com.example.testfirebase

data class User(
    var id: String = (User.id++).toString(),
    var name: String = "",
    var age: Int = 0
) {

    //just for creating unique primary key- for each value
    companion object {
        var id  = 0;
    }

}