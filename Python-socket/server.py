import threading
from hashlib import md5
import collections
import socket
import Queue
import sys


class ThreadHandler(threading.Thread):
    def __init__(self, connection_input):
        threading.Thread.__init__(self)
        self.connection_input = connection_input

    def run(self):
        while True:
            socket, address = self.connection_input.get()
            handle_chat_room(socket, address)
            self.connection_input.task_done()

def handle_chat_room(socket, address):
    while True:
        message = socket.recv(2048).decode('utf-8')
        if message.startswith("KILL_SERVICE"):
            socket.close()
            break

        elif message.startswith("HELO"):
            socket.sendall("{0}\nIP:{1}\nPort:{2}\nStudentID:{3}".format(message.strip(), ipaddress, port_num, student_num))
            continue

        message = message.split('\n')
        message_action_key = message[0]
        message_action = message_action_key[:message_action_key.find(':')]
        if (message_action == 'CHAT'):
            chat_room_id = int(message[0].split(":")[1])
            chat_room_join_identifier = int(message[1].split(":")[1])
            client_name = message[2].split(":")[1]
            broadcast(chat_room_id, "CHAT:{0}\nCLIENT_NAME:{1}\nMESSAGE:{2}\n\n".format(str(chat_room_id), str(client_name), message[3].split(":")[1]))
        elif (message_action == 'JOIN_CHATROOM'):
            client_name = message[3].split(":")[1]
            chat_room_name = message[0].split(":")[1]
            chat_room_identifier = int(md5(chat_room_name).hexdigest(), 16)
            chat_room_join_identifier = int(md5(client_name).hexdigest(), 16)
            if chat_room_identifier not in chat_rooms:
                chat_rooms[chat_room_identifier] = dict()
            if chat_room_join_identifier not in chat_rooms[chat_room_identifier]:
                chat_rooms[chat_room_identifier][chat_room_join_identifier] = socket
                socket.sendall("JOINED_CHATROOM:{0}\nSERVER_IP:{1}\nPORT:{2}\nROOM_REF:{3}\nJOIN_ID:{4}\n".format(str(chat_room_name), address[0], address[1], str(chat_room_identifier), str(chat_room_join_identifier)))
                broadcast(chat_room_identifier, "CHAT:{0}\nCLIENT_NAME:{1}\nMESSAGE:{2}".format(str(chat_room_identifier), str(client_name), str(client_name) + " has joined this chatroom.\n\n"))

        elif (message_action == 'LEAVE_CHATROOM'):
            chat_room_id = int(message[0].split(":")[1])
            chat_room_join_identifier = int(message[1].split(":")[1])
            client_name = message[2].split(":")[1]
            socket.sendall("LEFT_CHATROOM:{0}\nJOIN_ID:{1}\n".format(str(chat_room_id), str(chat_room_join_identifier)))
            broadcast(chat_room_id, "CHAT:{0}\nCLIENT_NAME:{1}\nMESSAGE:{2}\n\n".format(str(chat_room_id), str(client_name), str(client_name) + " has left this chatroom."))
            del chat_rooms[chat_room_id][chat_room_join_identifier]

        elif (message_action == 'DISCONNECT'):
            client_name = message[2].split(":")[1]
            chat_room_join_identifier = int(md5(client_name).hexdigest(), 16)
            for chat_room_id in chat_rooms.keys():
                if chat_room_join_identifier in chat_rooms[chat_room_id]:
                    broadcast(chat_room_id, "CHAT:{0}\nCLIENT_NAME:{1}\nMESSAGE:{2}\n\n".format(str(chat_room_id), str(client_name), str(client_name) + " has left this chatroom."))
                    if chat_room_join_identifier in chat_rooms[chat_room_id]:
                        del chat_rooms[chat_room_id][chat_room_join_identifier]
            break

def broadcast(chat_room_id, data):
    for room_join_identifier, connection in chat_rooms[chat_room_id].iteritems():
        connection.sendall(data)


ipaddress = socket.gethostbyname(socket.gethostname())
port_num = int(sys.argv[1])
student_num = 17317151
connection_pool = Queue.Queue(maxsize=100)
chat_rooms = collections.OrderedDict()
serv_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serv_sock.bind((ipaddress, port_num))
serv_sock.listen(5)

while True:
    connection, address = serv_sock.accept()
    handle_connection = ThreadHandler(connection_pool)
    handle_connection.setDaemon(True)
    handle_connection.start()
    connection_pool.put((connection, address))




