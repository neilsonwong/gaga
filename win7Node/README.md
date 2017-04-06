TODO:
    add support for :
        clementine(partial)
        router things
            who's using bandwidth (nice to have)
            who's home (ping ips)

DONE:
    audio switching
    media player classic
    computer functions
        turn on
        turn off



running as a service
p.s.

I found the thing so useful that I built an even easier to use wrapper around it (npm, github).

Installing it:

npm install -g qckwinsvc
Installing your service:

> qckwinsvc
prompt: Service name: [name for your service]
prompt: Service description: [description for it]
prompt: Node script path: [path of your node script]
Service installed
Uninstalling your service:

> qckwinsvc --uninstall
prompt: Service name: [name of your service]
prompt: Node script path: [path of your node script]
Service stopped
Service uninstalled




TODO: 
    configurations

    android:
        screen off scenario

        extra functions
        UI

        fix install thing

        play music/media