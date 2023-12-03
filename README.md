A simple discord bot to host a secret santa. This bot can handle multiple different servers simultaneously, but is not 
built very well, so I wouldn't run it on too many server.

# How to use
## Setup
After setting up the bot and adding it to your server, you can kick of the secret santa with:
```
/prime-santa <date> [duedate] [channel]
```

This will post a message that mentions everyone and explains the secret santa and how to opt in and out.
The `date` parameter is required and specifies when people will be assigned their gift target. This is not enforced, and
is just for the same of the message.

The `duedate` parameter is optional, and allows you to add to the message a section about when to have gifts arrive for.

Both the `date` and `duedate` parameters are strings. I would recommend using [a timestamp generator](https://r.3v.fi/discord-timestamps/)
to create a timezone-adjusted message.

Finally, the channel parameter is optional, using it adds to the message a part mentioning the channel and specifying 
that it is for people to discuss and post their steam profiles for gift delivery.

The created message will have buttons that allow users to opt-in and opt-out.

## Beginning
To start the gift allocation, use:
```
/start-santa
```
This will allocate everyone a gift target and PM them the gift target's discord username

## Resetting
To reset the server's secret santa, you can use:
```
/reset-santa
```
This will clear the users that have opted in

## Peeking at the users who've opted in
You can get a list (that only you can see) of the users that have opted into the secret santa with
```
/list-santa
```

# Setup
## Building
To build the bot, clone the repo and run
```shell
./gradlew build
```
to build the bot.
You can then find the bot in `./build/libs/secret-santa-bot-[VERSION]-all.jar`

## Running
To run the bot, setup an application on the [Discord Developer Portal](https://discord.com/developers), then execute the
bot jar on at least java 11, passing your bot's token with the Environment Variable: `SECRET_SANTA_TOKEN`.

Ensure your bot has permissions to:
- Send Messages
- Mention Everyone
- Use Slash Commands