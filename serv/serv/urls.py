from django.conf.urls import patterns, url
from hack import settings
from hack_serv import views

urlpatterns = patterns('',
                       url(r'reg/$', views.reg, name='reg'),
                       url(r'login/$', views.login, name='login'),
                       url(r'recogn/$', views.recogn, name='recogn'),
                       url(r'send_message/$', views.send_message, name='send_message'),
                       url(r'check_message/$', views.check_message, name='check_message'))