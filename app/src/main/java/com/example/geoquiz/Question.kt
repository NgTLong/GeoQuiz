package com.example.geoquiz

class Question(textResId : Int, answerTrue : Boolean) {
    var textResId : Int = textResId
    var answerTrue : Boolean = answerTrue
    var hasBeenAnswered : Boolean = false
}