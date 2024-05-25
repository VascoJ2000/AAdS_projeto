const chatInput = document.getElementById('message')
const chatHistory = document.getElementById('chat-history')
const loading = document.getElementById('loading')
const currentChat = null

let stompClient = null

function connect(e) {
    const socket = new SockJS('/chat-socket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnect, onError);
}

function onConnect() {
    stompClient.subscribe('/topic/public', mesReceived);
    /*
    const chats = getChatList()

    for (let chat in chats) {
        stompClient.subscribe(`/topic/${chat.id}`, mesReceived);
    }
    */
    stompClient.send(
        "/app/chat.addUser",
        {},
        JSON.stringify({})
    )
    loading.style.display = 'none'
}

function onError() {
    loading.style.display = 'block'
    loading.textContent = 'Could not load chat. Pls try again!'
    loading.style.color = 'red'
}

function getChatList() {
    let chats = null
    fetch(url + `/api/chat`, {
        method: 'GET',
    })
        .then(res => res.json())
        .then(data => {
            console.log(data)
            chats = data
        })
        .catch(err => console.log(err))

    return chats
}

async function getChat(chat) {
    await fetch(url + `/api/chat/${chat}`, {
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
    await fetch(url + `/api/chat`, {
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

function loadChat(chatRoom){
    activateChat(chatRoom)

}

function activateChat(chatRoom) {
    loading.textContent = "Loading chat..."
    loading.style.display = "block"

    const rooms = document.getElementsByTagName('chat-room')

    for(let i = 0; i<rooms.length; i++){
        rooms[i].classList.add('text-white')
        rooms[i].classList.remove('active')
    }

    document.getElementById(chatRoom).classList.remove('text-white')
    document.getElementById(chatRoom).classList.add('active')
}

function addChatToList(chat){

}

function mesReceived(){

}
