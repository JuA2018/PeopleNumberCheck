package org.techtown.peoplenumbercheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.dataText)
    EditText dataText;

    @OnClick(R.id.button)
    public void juminbutton(){
        String juminNo = editText.getText().toString();
        String regex = "[0-9]{6}-[0-9]{7}";
        boolean check = Pattern.matches(regex,juminNo);

        if (check == false) {
            Toast.makeText(this, "주민번호가 정규 표현식에 맞지 않음", Toast.LENGTH_SHORT).show();
        } else {
            int sum = 0;
            int weight[] = {2, 3, 4, 5, 6, 7, 0, 8, 9, 2, 3, 4, 5};
            int tem, result;

            for (int i = 0; i < 13; i++) {
                if (juminNo.charAt(i) == '-')
                    continue;
                sum += (juminNo.charAt(i)-48) * weight[i];
            }

            tem = 11 - (sum % 11);
            result = tem % 10;

            if (result == juminNo.charAt(13)-48) {
                Toast.makeText(this, "주민번호가 정상 임", Toast.LENGTH_SHORT).show();

                //나이 추출
                Calendar calendar = Calendar.getInstance(Locale.KOREAN);
                int year = calendar.get(Calendar.YEAR);
                int yy = Integer.parseInt(juminNo.substring(0,2));

                if (juminNo.charAt(7) - 48 < 3){
                    yy = yy + 1900;
                } else {
                    yy = yy + 2000;
                }
                int age = year - yy + 1;
                dataText.append("나이 : " + age + '\n');

                //성별 추출
                if ((juminNo.charAt(7) - 48) % 2 != 0)
                    dataText.append("성별 : 남자" + '\n');
                else
                    dataText.append("성별 : 여자" + '\n');

                //태어난 위치 추출
                String localeCode[][] = {{"서울","00","08"},{"부산","09","12"}
                        ,{"인천","13","15"},{"경기","16","25"}
                        ,{"강원","26","34"},{"충북","35","39"}
                        ,{"대전","40","40"},{"충남","41","43"}
                        ,{"충남","45","47"},{"세종","44","44"}
                        ,{"세종","96","96"},{"전북","48","54"}
                        ,{"전남","55","64"},{"광주","65","66"}
                        ,{"대구","67","70"},{"경북","71","80"}
                        ,{"경남","81","84"},{"경남","86","90"}
                        ,{"울산","85","85"},{"제주","91","95"}};

                String localStr = juminNo.substring(8,10);
                int locale = Integer.parseInt(localStr);

                String birthplace = null;

                for(int j=0; j<=19; j++){
                    if (locale >= Integer.parseInt(localeCode[j][1]) && locale <= Integer.parseInt(localeCode[j][2])){
                        birthplace = localeCode[j][0];
                        dataText.append("출신도 : " + birthplace + '\n');
                        break;
                    }else {
                        continue;
                    }
                }

                //띠 추출
                String gangi[]={"원숭이","닭","개","돼지","쥐","소","호랑이","토끼","용","뱀","말","양"};
                String ddi = null;
                ddi = gangi[yy%12];
                dataText.append("띠 : " + ddi + '\n');

                //생년월일 추출
                dataText.append("생년월일 : " + yy + '/' + juminNo.substring(2,4) + '/' +juminNo.substring(4,6));

            }else {
                Toast.makeText(this, "주민번호가 맞지 않음.", Toast.LENGTH_SHORT).show();
                editText.setText("");
                dataText.requestFocus();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }
}
