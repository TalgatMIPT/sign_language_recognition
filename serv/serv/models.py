from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User
from django.contrib import auth


class Messages(models.Model):
    client_s = models.ForeignKey('auth.User', related_name='client_s')
    client_r = models.ForeignKey('auth.User', related_name='client_r')
    message = models.TextField()
    message_date = models.DateTimeField(blank=True, null=True)
    is_received = models.BooleanField(blank=True, default=True)

    def send(self):
        self.message_date = timezone.now()
        self.save()

    def __str__(self):
        return self.message
