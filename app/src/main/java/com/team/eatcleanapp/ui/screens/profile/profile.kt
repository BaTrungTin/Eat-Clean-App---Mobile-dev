package com.team.eatcleanapp.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.team.eatcleanapp.R


@Composable
// trang topbar contrainlayout
fun TopBar(
    modifier: Modifier= Modifier

){
    ConstraintLayout {
        val (tvName,imgName,tvBMI)=createRefs()

        Text(text = "Nguyễn Hồng Đông ", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = Color(0xFF000000)
        ),
            modifier= Modifier.constrainAs(tvName){
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 20.dp)

            }
        )
        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = "avartar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .constrainAs(imgName){
                    top.linkTo(parent.top, margin = 12.dp)
                    start.linkTo(tvName.end, margin = 16.dp)
                }


        )
        Text(text = "Chỉ số cơ thể (BMI)", style = TextStyle(
            fontWeight = FontWeight.SemiBold

        ),
            modifier= Modifier.constrainAs(tvBMI){
                top.linkTo(tvName.bottom, margin = 20.dp)
                start.linkTo(tvName.start)

            }
        )



    }
}

@Composable
fun BodyBMI(
    name:String,
    dateTime:String,
    value: Double,
    unit:String?,




    modifier: Modifier = Modifier
){


    val Green = Color(color = 0xFF046E1F).copy(0.5f)
    val Black= Color(color = 0xFF000000)
    val Red = Color(color=0xFFC11F1F)


    ConstraintLayout(
        modifier=modifier
            .fillMaxWidth()
            .padding(horizontal=16.dp)
            .background(color = Green, shape = RoundedCornerShape(15))
            .height(152.dp)

    )

    {

        val verticalGuideLine25 = createGuidelineFromStart(0.25f)
        val (textNameRef,iconRef, textDateTimeRef, textValueRef) = createRefs()

        Text(
            text = name,
            color = Black,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier.constrainAs(textNameRef) {
                top.linkTo(parent.top, margin = 36.dp)
                start.linkTo(parent.start, margin = 20.dp)

            }
        )
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint=Black , // dùng để tô màu cho Icon
            modifier = Modifier
                .size(20.dp)
                .constrainAs(iconRef){
                    top.linkTo(parent.top, margin = 45.dp)
                    start.linkTo(textNameRef.end , margin = 15.dp)

                }

        )

        Text(
            text = dateTime,
            color = Black,
            fontSize = 20.sp,
            modifier= Modifier.constrainAs(textDateTimeRef){
                top.linkTo(iconRef.top)
                start.linkTo(iconRef.end, margin = 5.dp)
            }
        )
        Text(
            text = "${value}${unit ?: ""}",
            color = Red,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier= Modifier
                .constrainAs(textValueRef){
                    top.linkTo(textNameRef.bottom, margin = 15.dp)
                    start.linkTo(textNameRef.start)

                }
        )



    }

}
@Composable
fun ScreenProfile(){
    Column (modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)){
        BodyBMI(
            name="BMI",
            dateTime="14 tháng 8 -11:00",
            value=18.5,
            unit=null,
            modifier = Modifier

        )
        Spacer(modifier = Modifier.height(20.dp))
        BodyBMI(
            name="Chiều cao",
            dateTime="14 tháng 8 -11:00",
            value=180.0,
            unit="cm",
            modifier = Modifier

        )
        Spacer(modifier = Modifier.height(20.dp))
        BodyBMI(
            name="Cân nặng",
            dateTime="14 tháng 8 -11:00",
            value=60.0,
            unit="kg",
            modifier = Modifier

        )



    }


}
@Preview(showBackground = true)
@Composable
fun  BodyBMI(){
    val BackgroundColor= Color(0xFF225416).copy(0.2f)
    Column (modifier = Modifier
        .background(BackgroundColor)
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .padding(vertical = 20.dp)

    ){
        TopBar()
        Spacer(modifier = Modifier.height(25.dp))
        ScreenProfile()

    }


}