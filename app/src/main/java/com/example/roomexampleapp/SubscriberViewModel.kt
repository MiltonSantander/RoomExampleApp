package com.example.roomexampleapp

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomexampleapp.db.Subscriber
import com.example.roomexampleapp.db.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubscriberViewModel(private val subscriberRepository: SubscriberRepository) : ViewModel() {
    val subscribers = subscriberRepository.subscribers
    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private var itemHasBeenSelected = false
    private lateinit var selectedSubscriber: Subscriber
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            setStatusMessage("Please enter a name")
        } else if (inputEmail.value == null) {
            setStatusMessage("Please enter an email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).find()) {
            setStatusMessage("Please enter a valid email")
        } else {
            if (itemHasBeenSelected) {
                selectedSubscriber.name = inputName.value.toString()
                selectedSubscriber.email = inputEmail.value.toString()
                update(selectedSubscriber)
            } else {
                insert(
                    Subscriber(
                        0,
                        inputName.value.toString(),
                        inputEmail.value.toString()
                    )
                )
                inputName.value = ""
                inputEmail.value = ""
            }
        }
    }

    fun clearAllOrDelete() {
        if (itemHasBeenSelected) {
            delete(selectedSubscriber)
        } else {
            clearAll()
        }
    }

    private fun insert(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            val newSubscriberId = subscriberRepository.insert(subscriber)
            withContext(Dispatchers.Main) {
                if (newSubscriberId > -1) {
                    setStatusMessage("Subscriber Inserted Successfully")
                } else {
                    setStatusMessage("Error Occurred")
                }
            }
        }
    }

    private fun update(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfRows = subscriberRepository.update(subscriber)
            if (numberOfRows > 0) {
                setComponentsToStartValue("Subscriber Updated Successfully")
            } else {
                setComponentsToStartValue("Error Occurred")
            }
        }
    }

    private fun delete(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfRowsDeleted = subscriberRepository.delete(subscriber)
            if (numberOfRowsDeleted > 0) {
                setComponentsToStartValue("Subscriber Deleted Successfully")
            } else {
                setComponentsToStartValue("Error Occurred")
            }
        }
    }

    private fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfRowsDeleted = subscriberRepository.deleteAll()
            withContext(Dispatchers.Main) {
                if (numberOfRowsDeleted > 0) {
                    setStatusMessage("All Subscribers Deleted Successfully")
                } else {
                    setStatusMessage("Error Occurred")
                }
            }
        }
    }

    fun selectedItem(subscriber: Subscriber) {
        inputEmail.value = subscriber.email
        inputName.value = subscriber.name
        saveOrUpdateButtonText.value = "update"
        clearAllOrDeleteButtonText.value = "delete"
        itemHasBeenSelected = true
        selectedSubscriber = subscriber

    }

    private suspend fun setComponentsToStartValue(statusMessageString: String) {
        withContext(Dispatchers.Main) {
            inputEmail.value = ""
            inputName.value = ""
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            itemHasBeenSelected = false
            setStatusMessage(statusMessageString)
        }
    }

    private fun setStatusMessage(statusMessageString: String) {
        statusMessage.value = Event(statusMessageString)
    }
}