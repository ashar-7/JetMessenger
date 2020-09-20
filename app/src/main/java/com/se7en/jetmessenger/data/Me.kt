package com.se7en.jetmessenger.data

import com.se7en.jetmessenger.data.models.Name
import com.se7en.jetmessenger.data.models.Picture
import com.se7en.jetmessenger.data.models.User

val me = User(
    Name(
        first = "John",
        last = "Doe",
        title = "Mr"
    ),
    Picture(
        thumbnail = "https://picsum.photos/id/1005/45/45",
        medium = "https://picsum.photos/id/1005/72/72",
        large = "https://picsum.photos/id/1005/128/128"
    )
)
