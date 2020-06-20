
import 'package:flutter/material.dart';


final greenL = new Color(0xFF2ecc71);
final greenD = new Color(0xFF27ae60);
final greyL = new Color(0xFF7f8c8d);
final greyM = new Color(0xFF34495e);
final greyD = new Color(0xFF2c3e50);
final redL = new Color(0xFFe74c3c);
final redD = new Color(0xFFc0392b);
final purpleL = new Color(0xFF9b59b6);
final purpleD = new Color(0xFF8d44ad);


final marchatTheme = ThemeData(
  accentColor: purpleL,
  accentTextTheme: TextTheme(
    bodyText1: TextStyle(color: Colors.white),
    caption: TextStyle(color: greenL)
  ),
  cardColor: greyM,

  buttonColor: greenL,
  buttonTheme: ButtonThemeData(
    buttonColor: greenL,
    focusColor: greenD,
    disabledColor: greyL, 
  ),

  brightness: Brightness.dark,
  bottomAppBarColor: greyM,
  backgroundColor: greyD,

  snackBarTheme: SnackBarThemeData(
    actionTextColor: greenL,
    backgroundColor: greyD,
  ),

  visualDensity: VisualDensity.adaptivePlatformDensity,
);