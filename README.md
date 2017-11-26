# Sign language recognition project

## Description
Uses Android device for capturing video frame and sends the best frame to the Django server, which defines showing symbol by
machine learning algorithms.

## Implementation
This project contains 3 modules: Android app, Python app, that uses machine learning algorithms and Python Django app that implements the server. Android app captures frames from video frame, takes frame with the best quality and then sends it to the server. After the response is got application continious to capture frames. Django app gets the frame encrypted by Base64, decrypt it and sends to other Python application with machine learning. This app uses algorithms for recognition circuit of hand and tries to define showing symbol. After that it sends callback to the server that contatins symbol and then the server returns it to Android app. This result is written in buffer. Also we implemented simple authentication and chat system. We just wanted to show how it can work.

## Git
We used 5 branches for this project:<br>
1)Master branch<br>
2)talgat branch: contatins server commits<br>
3)android branch: contains Android app commits<br>
4)ilya_ml: contains Python app commits by 1st machine learning developer<br>
5)ML_Nastya: contatins Python app commits by 2nd machine learning developer

## Result and furtherance
At the moment we can not get the result we want, because of lack of machine learning knowledge. But we got perfect Android and server apps. Further our team wants to continue this project because this idea is very interesting and not implemented yet by anyone. We understand that there are some difficults with this idea shared with that our app has to understand many symbols per second and there may be a lot of inaccuracies of showed symbols. But we want to try.
