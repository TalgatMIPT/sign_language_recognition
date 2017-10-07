import os
from django.shortcuts import render
from django.contrib.auth.models import User
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth import authenticate, login
from django.contrib import auth
from django.contrib.auth.forms import UserCreationForm
import base64
from PIL import Image
from io import BytesIO
import json


@csrf_exempt
def reg(request):
    if request.method == 'POST':
        json_string = str((request.body).decode("utf-8"))
        parsed_string = json.loads(json_string)

        username = parsed_string.get('username')
        password = parsed_string.get('password')

        # username = request.POST.get('username','')
        # password = request.POST.get('password','')
        print("___try reg: "+username+" ____")
        ifolduser = auth.authenticate(username=username, password=password)

        if ifolduser is not None:
            print("\nREG_NOT_ALLOWED")
            return HttpResponse("Fail")

        newuser = User.objects.create_user(username, None, password)
        newuser.save()
        newuser = auth.authenticate(username=username, password=password)

        # auth.login(request, newuser)
        return HttpResponse("OK")
    return HttpResponse("Fail")


@csrf_exempt
def login(request):
    if request.method == 'POST':
        json_string = str((request.body).decode("utf-8"))
        parsed_string = json.loads(json_string)

        username = parsed_string.get('username')
        password = parsed_string.get('password')

        print("___try login: "+username+" _____")

        user = auth.authenticate(username=username, password=password)

        if user is not None:
            auth.login(request, user)
            return HttpResponse("OK")
        else:
            print("\nLOGIN NOT ALLOWED")
    return HttpResponse("Fail")

@csrf_exempt
def recogn(request):
    print("in recogn")
    if request.method == 'POST':
        # print(request.body)
        print("RECOGN")
        image64 = str(((request.body).decode("utf-8")))
        # print("\n\n"+image64+"\n\n")

        parsed_image64 = (json.loads(image64)).get('img')
        # parsed_image64 = bytes(parsed_image64.decode("base64"))

        print(parsed_image64)
        im = Image.open(BytesIO(base64.b64decode(parsed_image64)))
        rgb_im = im.convert('RGB')
        rgb_im.save('temp_images/tr.jpeg')
        os.remove('temp_images/tr.jpeg')
        return HttpResponse("OK")
    return HttpResponse("Fail")

