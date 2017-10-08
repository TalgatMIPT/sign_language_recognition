# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        ('serv', '0002_messages_is_received'),
    ]

    operations = [
        migrations.AlterField(
            model_name='messages',
            name='client_r',
            field=models.ForeignKey(related_name='client_r', to=settings.AUTH_USER_MODEL),
        ),
        migrations.AlterField(
            model_name='messages',
            name='client_s',
            field=models.ForeignKey(related_name='client_s', to=settings.AUTH_USER_MODEL),
        ),
    ]
