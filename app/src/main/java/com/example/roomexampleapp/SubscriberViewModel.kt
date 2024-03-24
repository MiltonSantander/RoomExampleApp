package com.example.roomexampleapp

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
    val subscriber = subscriberRepository.subscriber
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

    fun clearAllOrDelete() {
        if (itemHasBeenSelected) {
            delete(selectedSubscriber)
        } else {
            clearAll()
        }
    }

    private fun insert(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.insert(subscriber)
            withContext(Dispatchers.Main) {
                setStatusMessage("Subscriber Inserted Successfully")
            }
        }
    }

    private fun update(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.update(subscriber)
            setComponentsToStartValue("Subscriber Updated Successfully")
        }
    }

    private fun delete(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.delete(subscriber)
            setComponentsToStartValue("Subscriber Deleted Successfully")
        }
    }

    private fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.deleteAll()
            withContext(Dispatchers.Main) {
                setStatusMessage("All Subscribers Deleted Successfully")
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