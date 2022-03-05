package com.example.geoquiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"
private const val KEY_INDEX: String = "index"
private const val KEY_ANSWERED: String = "questionsAnswered"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private var currentIndex: Int = 0
    private var correctAnswerCount: Int = 0

    private val questionBank = arrayOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX)
            savedInstanceState.getBooleanArray(KEY_ANSWERED)?.let { setQuestionsAnsweredState(it) }
        }

        questionTextView = findViewById(R.id.question_text_view)
        updateQuestion()

        trueButton = findViewById(R.id.true_button)
        trueButton.setOnClickListener {
            checkAnswer(true)
            setQuestionAnswered()
            toggleButtons()
            if (checkIfAllAnswered()) {
                displayQuizScore()
            }
        }

        falseButton = findViewById(R.id.false_button)
        falseButton.setOnClickListener {
            checkAnswer(false)
            setQuestionAnswered()
            toggleButtons()
            if (checkIfAllAnswered()) {
                displayQuizScore()
            }
        }

        prevButton = findViewById(R.id.prev_button)
        prevButton.setOnClickListener  {
            if(currentIndex > 0){
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
                toggleButtons()
            }
            else{
                questionBank.size - 1
            }
            updateQuestion()
            toggleButtons()
        }

        nextButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            toggleButtons()
        }
        toggleButtons()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState?.putInt(KEY_INDEX, currentIndex)
        outState?.putBooleanArray(KEY_ANSWERED, getQuestionsAnsweredState())
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answerTrue
        val messageResId = if (userAnswer == correctAnswer) {
            correctAnswerCount += 1
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

    private fun setQuestionAnswered() {
        questionBank[currentIndex].hasBeenAnswered = true
    }

    private fun getQuestionsAnsweredState(): BooleanArray {
        val questionsAnsweredState = arrayListOf<Boolean>()
        questionBank.forEach { question ->
            questionsAnsweredState.add(question.hasBeenAnswered)
        }
        return questionsAnsweredState.toBooleanArray()
    }

    private fun setQuestionsAnsweredState(answeredState: BooleanArray) {
        questionBank.forEachIndexed { index, question ->
            question.hasBeenAnswered = answeredState[index]
        }
    }

    private fun toggleButtons() {
        trueButton.isEnabled = !questionBank[currentIndex].hasBeenAnswered
        falseButton.isEnabled = !questionBank[currentIndex].hasBeenAnswered
    }

    private fun checkIfAllAnswered(): Boolean {
        val unansweredQuestions: List<Question> =
            questionBank.filter { question -> !question.hasBeenAnswered }

        return unansweredQuestions.isEmpty()
    }

    private fun displayQuizScore() {
        Log.d(TAG, "correctAnswerCount: ".plus(correctAnswerCount))
        Log.d(TAG, "questionBank.size: ".plus(questionBank.size))
        val quizScore: Float =
            correctAnswerCount.toFloat() / questionBank.size.toFloat()
        Log.d(TAG, "quizScore: ".plus(quizScore))
        val scoreMessage: String = "You scored "
            .plus(Math.round(quizScore * 100))
            .plus("% on this quiz!")

        Toast.makeText(this,
            scoreMessage,
            Toast.LENGTH_SHORT).show()
    }
}
