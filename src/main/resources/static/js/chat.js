const chatInput = document.getElementById('message')
const chatHistory = document.getElementById('chat-history')
const currentChat = null

async function getChatList() {
    await fetch(url + `/api/auth/chat`, {
        method: 'GET',
    })
        .then(res => res.json())
        .then(data => {
            addChatToList(data)
            console.log(data)
        })
        .catch(err => console.log(err))
}

async function getChat(chat) {
    await fetch(url + `/api/auth/chat/${chat}`, {
        method: 'GET',
    })
        .then(res => res.json())
        .then(data => {
            loadChat(data)
            console.log(data)
        })
        .catch(err => console.log(err))
}

async function addChat() {
    const newChat = null
    await fetch(url + `/api/auth/chat`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            newChat
        }),
    })
        .then(res => res.json())
        .then(data => {
            loadChat(data)
            console.log(data)
        })
        .catch(err => console.log(err))
}

async function sendMessage() {
    if (currentChat === null) return alert('No Chat is currently selected!')
    const chat = currentChat.value
    const mes = chatInput.value
    await fetch(url + `/api/auth/chat/mes`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            message: mes,
            chatId: chat
        }),
    })
        .then(res => res.json())
        .then(data => {
            loadChat(data)
            console.log(data)
        })
        .catch(err => console.log(err))
}

async function addUser() {
    const email = null
    await fetch(url + `/api/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email
        }),
    })
        .then(res => res.json())
        .then(data => {
            loadChat(data)
            console.log(data)
        })
        .catch(err => console.log(err))
}

function addToChat(mes){

}

function loadChat(){

}

function addChatToList(chat){

}
