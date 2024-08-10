package com.example.firstapplication

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var baseAmount: EditText
    private lateinit var tipSeekBar: SeekBar
    private lateinit var tipPercentLabel: TextView
    private lateinit var tipValue: TextView
    private lateinit var totalValue: TextView
    private lateinit var tipDescription: TextView
    private lateinit var memberSeekBar: SeekBar
    private lateinit var memberLabel: TextView
    private lateinit var memberShare: TextView
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var splitBill: Switch
    private lateinit var splitView: CardView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        baseAmount = findViewById(R.id.billAmount)
        tipSeekBar = findViewById(R.id.tvTipPercentValue)
        tipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tipValue = findViewById(R.id.tvTipValue)
        totalValue = findViewById(R.id.tvTotalValue)
        tipDescription = findViewById(R.id.tipAdjective)
        memberSeekBar = findViewById(R.id.memberSeekBar)
        memberLabel = findViewById(R.id.tvMemberLabel)
        memberShare = findViewById(R.id.tvMemberShare)
        splitBill = findViewById((R.id.splitBill))
        splitView = findViewById(R.id.SplitView)

        tipSeekBar.progress = INITIAL_TIP_PERCENT
        tipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        tipSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                tipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
                computeMemberShare(memberSeekBar.progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        memberSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                memberLabel.text = "$p1"
                computeMemberShare(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        baseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                computeTipAndTotal()
            }

        })

        splitBill.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    splitView.visibility = View.VISIBLE

                } else {
                    splitView.visibility = View.INVISIBLE

                }
            }
        }
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDesc = when(tipPercent) {
            in 0..9 -> "Poor"
            in 10 .. 14 -> "Acceptable"
            in 15 .. 19 -> "Good"
            in 20 .. 24 -> "Great"
            else -> "Amazing"
        }
        tipDescription.text = tipDesc
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/tipSeekBar.max,
            ContextCompat.getColor(this,R.color.worst_Color),
            ContextCompat.getColor(this,R.color.best_Color)
        ) as Int
        tipDescription.setTextColor(color)
    }

    @SuppressLint("SetTextI18n")
    private fun computeTipAndTotal() {
        if(baseAmount.text.isEmpty()){
            tipValue.text = ""
            totalValue.text = ""
            splitBill.visibility = View.INVISIBLE
            return
        }
        else {
            val billAmount = baseAmount.text.toString().toDouble()
            val tipPercent = tipSeekBar.progress
            val tipAmount = (billAmount * tipPercent.toFloat()/ 100)
            val totalAmount = (billAmount + tipAmount)

            tipValue.text = "%.2f".format(tipAmount)
            totalValue.text = "%.2f".format(totalAmount)
            splitBill.visibility = View.VISIBLE
        }

    }

    @SuppressLint("SetTextI18n")
    private fun computeMemberShare(memberSize: Int) {
        if(totalValue.text.isEmpty() || memberSize == 0) {
            memberShare.text = ""
            return
        }
        else {
            val totalBill = totalValue.text.toString().toFloat()
            val memberShareAmount = totalBill/memberSize
            Log.i("MemberShare", memberShareAmount.toString())
            memberShare.text = "%.2f".format(memberShareAmount)
        }

    }
}