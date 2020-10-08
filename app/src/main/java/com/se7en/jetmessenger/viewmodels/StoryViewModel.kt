package com.se7en.jetmessenger.viewmodels

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.se7en.jetmessenger.data.models.Story
import com.se7en.jetmessenger.data.models.StoryStatus
import com.se7en.jetmessenger.data.models.User
import kotlin.random.Random

class StoryViewModel: ViewModel() {

    val stories = mutableStateMapOf<User, Story>()

    fun getStories(users: List<User>) =
        users.map { user -> getStory(user) }.sortedWith { o1, o2 ->
            // sort the list so that the unseen stories appear first
            return@sortedWith when {
                o1.status == o2.status -> 0
                o1.status == StoryStatus.AVAILABLE_NOT_SEEN -> -1
                o2.status == StoryStatus.AVAILABLE_NOT_SEEN -> 1
                else -> 0
            }
        }

    fun updateStoryStatus(user: User, newStatus: StoryStatus) {
        getStory(user).status = newStatus
    }

    private fun getStory(user: User) =
        stories.getOrPut(user, {
            val id = Random.nextInt(1, 1000)
            val hour = Random.nextInt(1, 24)
            Story(
                user = user,
                status = StoryStatus.AVAILABLE_NOT_SEEN,
                imageUrl = "https://picsum.photos/id/$id/300/300",
                thumbnailUrl = "https://picsum.photos/id/$id/100/150",
                time = "${hour}h"
            )
        })
}
