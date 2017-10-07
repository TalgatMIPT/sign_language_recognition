from django.conf.urls import include, url
from django.contrib import admin
from serv import views

urlpatterns = [
    # Examples:
    # url(r'^$', 'mysite.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'reg/$', views.reg, name='reg'),
    url(r'login/$', views.login, name='login'),
    url(r'recogn/$', views.recogn, name='recogn'),
    url(r'send_message/$', views.send_message, name='send_message'),
    url(r'check_message/$', views.check_message, name='check_message'),
]
