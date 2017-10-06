from django.conf.urls import patterns, url
from hack import settings
from hack_serv import views

urlpatterns = patterns('',
                       url(r'reg/$', views.reg, name='reg'),
                       url(r'login/$', views.login, name='login'))