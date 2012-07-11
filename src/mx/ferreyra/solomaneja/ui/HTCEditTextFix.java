package mx.ferreyra.solomaneja.ui;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

public class HTCEditTextFix implements TextWatcher{


	private EditText mEditText;
	public HTCEditTextFix(EditText editText) {
		mEditText = editText;
	}
	public void afterTextChanged(Editable s) {}
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mEditText.getText().setSpan(new ForegroundColorSpan(Color.WHITE), start, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
}
