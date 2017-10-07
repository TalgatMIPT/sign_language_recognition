import os
from operator import is_

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
from .models import Messages
import json
from django.shortcuts import get_object_or_404
from django.utils import timezone


@csrf_exempt
def reg(request):
    if request.method == 'POST':
        json_string = str((request.body).decode("utf-8"))
        parsed_string = json.loads(json_string)

        username = parsed_string.get('username')
        password = parsed_string.get('password')

        # username = request.POST.get('username','')
        # password = request.POST.get('password','')
        print("___try reg: " + username + " ____")
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

        print("___try login: " + username + " _____")

        user = auth.authenticate(username=username, password=password)

        if user is not None:
            auth.login(request, user)
            return HttpResponse("OK")
        else:
            print("\nLOGIN NOT ALLOWED")
    return HttpResponse("Fail")


@csrf_exempt
def recogn(request):
    if request.method == 'POST':
        # print(request.body)
        print("RECOGN")
        image64 = str(((request.body).decode("utf-8")))
        # print("\n\n"+image64+"\n\n")

        parsed_image64 = (json.loads(image64)).get('img')
        # parsed_image64 = bytes(parsed_image64.decode("base64"))

        # print(parsed_image64)
        im = Image.open(BytesIO(base64.b64decode(parsed_image64)))
        rgb_im = im.convert('RGB')
        rgb_im.save('temp_images/tr.jpeg')
        # os.remove('temp_images/tr.jpeg')

        return HttpResponse("OK")
    return HttpResponse("Fail")


@csrf_exempt
def send_message(request):
    if request.method == 'POST':
        req_data = str((request.body).decode('utf-8'))
        parsed_data = json.loads(req_data)

        # Messages = parsed_data.save(commit=False)
        client_s = str(parsed_data.get('client_s'))
        client_r = str(parsed_data.get('client_r'))

        # user_s = auth.authenticate(username=client_s)
        # user_r = auth.authenticate(username=client_r)
        user_s = User.objects.get(username=client_s)
        user_r = User.objects.get(username=client_r)
        print("\n\ntry to send: " + client_s + "-" + str(user_s))
        print("\n\ntry to receive: " + client_r + "-" + str(user_r))
        if (user_s is not None) and (user_r is not None):
            message = str(parsed_data.get('message'))
            mes_obj = Messages(client_s=user_s, client_r=user_r, message=message, is_received=False)

            mes_obj.message_date = timezone.now()
            # mes_obj.is_received = False
            mes_obj.save()
            return HttpResponse("OK")
        else:
            return HttpResponse("Wrong User")
            # parsed_data.save()
    return HttpResponse("Fail")


@csrf_exempt
def check_message(request):
    # message = get_object_or_404(Messages, )
    if request.method == 'POST':
        req_data = str(request.body.decode('utf-8'))
        parsed_data = json.loads(req_data)
        client_r = str(parsed_data.get('client_r'))

        print("CHECK_MESSAGE_TO:" + str(client_r))

        messages_inf = []
        # messages_inf.extend(get_object_or_404(Messages, client_r=client_r, is_received=False))

        messages_inf.extend(Messages.objects.get(client_r=client_r, is_received=False))

        print(str(Messages.objects.filter(client_r=client_r, is_received=False)))

        if messages_inf is None and messages_inf.size() is 0:
            return HttpResponse("None")
        if messages_inf[0] is None:
            return HttpResponse("None")

        messages_inf[0].is_received = True

        message_dict = {"client_s": str(messages_inf[0].client_s),
                        "client_r": str(messages_inf[0].client_r),
                        "message": str(messages_inf[0].message),
                        "message_date": str(messages_inf[0].message_date)}
        json_message_dict = json.dumps(message_dict)
        return HttpResponse(json_message_dict)
    return HttpResponse("Fail")
