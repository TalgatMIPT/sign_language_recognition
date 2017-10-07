# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Messages',
            fields=[
                ('id', models.AutoField(verbose_name='ID', primary_key=True, serialize=False, auto_created=True)),
                ('message', models.TextField()),
                ('message_date', models.DateTimeField(blank=True, null=True)),
                ('client_r', models.ForeignKey(related_name='+', to=settings.AUTH_USER_MODEL)),
                ('client_s', models.ForeignKey(related_name='+', to=settings.AUTH_USER_MODEL)),
                #('is_received', models.BooleanField())
            ],
        ),
    ]
