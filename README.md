# Discord-Twitch Integration

###### This project allows you to create a Discord-Twitch integration using a Java application. It sends messages from Discord to Twitch and vice versa.

# Getting Started

###### Follow these simple steps to set up and run the integration.

# Prerequisites

###### Before you begin, make sure you have the following:

###### Java Development Kit 17 or above (JDK)

###### Discord Bot Token

###### Twitch Bot Access Token

###### Streamelements Access Token

###### Configuration File (config.json)

# Configuration

###### Setup the Config file or if not exists add one

```json
{
  "discord": {
    "guildid": "YOUR_DISCORD_GUILD_ID",
    "channelid": "YOUR_DISCORD_CHANNEL_ID",
    "bot": {
      "token": "YOUR_DISCORD_BOT_TOKEN",
      "activity": "Listening to Twitch"
    }
  },
  "twitch": {
    "streamer-name": "YOUR_TWITCH_STREAMER_NAME",
    "bot": {
      "name": "YOUR_TWITCH_BOT_NAME",
      "access-token": "YOUR_TWITCH_ACCESS_TOKEN",
      "streamelements-token": "YOUR_STREAMELEMENTS_ACCESS_TOKEN"
    }
  },
  "filter": {
    "blacklist": [
      "hurensohn",
      "bastard",
      "wichser"
    ]
  }
}
```

###### Make sure to replace the placeholders (YOUR_DISCORD_GUILD_ID, YOUR_DISCORD_CHANNEL_ID, etc.) with your actual credentials and configuration values.

# Start the Program:

###### Run the Java application to start the Discord-Twitch integration.

# Done!

###### Your integration is now up and running, allowing messages to be sent between Discord and Twitch.

# Usage

###### Discord messages sent to the specified channel in the configured guild will be relayed to Twitch.

###### Twitch messages in the streamer's chat will be relayed to Discord.