const chatInput = document.getElementById('message')
const chatHistory = document.getElementById('chat-history')
const loading = document.getElementById('loading')
let currentChat = null

let stompClient = null

function connect() {
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
    const token = sessionStorage.getItem("token")
    const chatMes = {
        message: null,
        chatId: currentChat,
        token: token
    }

    stompClient.send(
        "/app/mes.addUser",
        {},
        JSON.stringify(chatMes)
    )
    loading.style.display = 'none'
    currentChat = 'public'
}

function onError() {
    loading.style.display = 'block'
    loading.textContent = 'Could not load chat. Pls try again!'
    loading.style.color = 'red'
}

function getChat() {
    fetch(url + "/api/chat", {
        method: 'GET',
    })
    .then(res => res.json())
    .then(data => {
        loadChat(data)
        console.log(data)
    })
    .catch(err => console.log(err))
}

function sendMessage() {
    if (currentChat === null) return alert('No Chat is currently selected!')
    const chat = currentChat
    const mes = chatInput.value.trim()
    const token = sessionStorage.getItem("token")

    if(mes){
        const chatMes = {
            message: mes,
            chatId: chat,
            token: token
        }
        // "token": sessionStorage.getItem("token")
        stompClient.send("/app/mes.send", {}, JSON.stringify(chatMes))
        chatInput.value = ''
    }
}

function loadChat(chat){
    const mes = chat.messages
    for (let i = 0; i<mes.length; i++) {
        console.log(mes[i])
        addToChat(mes[i])
    }
}

function mesReceived(payload){
    const mes = JSON.parse(payload.body)
    addToChat(mes)
}

function addToChat(mes){
    if (mes.type == 'USER'){
        if(mes.user_id == document.getElementById('userLogged').innerText){
            chatHistory.innerHTML += `<div class="message outgoing">
                                        <div><b>${mes.user_id}</b></div>
                                        <div>${mes.message}</div>
                                      </div>`
        }else{
            chatHistory.innerHTML += `<div class="message incoming">
                                        <div><b>${mes.user_id}</b></div>
                                        <div>${mes.message}</div>
                                      </div>`
        }
    }else{
        chatHistory.innerHTML += `<div class="message server mx-auto">
                                    <div>${mes.message}</div>
                                  </div>`
    }
}
