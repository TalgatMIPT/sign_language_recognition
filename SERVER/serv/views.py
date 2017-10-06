from django.shortcuts import render
from django.contrib.auth.models import User
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth import authenticate, login
from django.contrib import auth
from django.contrib.auth.forms import UserCreationForm
# from django.contrib.auth.base_user AbstractBaseUser, BaseUserManager
# Create your views here.


@csrf_exempt # work
def reg(request):
    if request.method == 'POST':

        username = request.POST.get('username','')
        password = request.POST.get('password','')
        print("___try reg: "+username+" ____")
        ifolduser = auth.authenticate(username=username, password=password)

        if ifolduser is not None:
            return HttpResponse("Fail")

        newuser = User.objects.create_user(username, None, password)
        newuser.save()
        newuser = auth.authenticate(username=username, password=password)

        # auth.login(request, newuser)
        return HttpResponse("OK")
    return HttpResponse("Fail")


@csrf_exempt
def login(request):
    if request.method == 'POST': # request.POST:
        # print(request.POST)
        username = request.POST.get('username','')
        password = request.POST.get('password','')
        print("___try login: "+username+" _____")
        # username = auth.get_user(request).username
        # password = auth.get_user(request).password
        # user = User.object.get(username=username)
        user = auth.authenticate(username=username, password=password)
        # user = User.objects.get(username=username)
        # user
        if user is not None:
            # auth.login(request, user)
            return HttpResponse("OK")
        else:
            print("\nNOT")
    return HttpResponse("Fail")
