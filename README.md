# AirSaverBot
TelegramBot to find flights offers

Welcome everybody to AirSaverBot; this is a TelegramBot developed with Java using the Amadeus Flights Offers API. If you wish to contribute or give your feed back feel free to drop your PR

![](C:\Users\bmora\Downloads\J2TbjzMaQ0cwRO8x5uuTZ.png)

## Technologies

- **Java 22** - Main programming language.
- **Maven** - Dependency management and project build tool.
- **Telegram Bots API** - Interface for interacting with Telegram.
- **Amadeus API** - Service for retrieving flight and airport information.



## Architecture

The bot follows a **Clean (Hexagonal) Architecture**, ensuring modular and flexible code.

- **Application Layer**: Manages business logic and bot rules.
- **Infrastructure Layer**: Implements external APIs (Amadeus) and handles communication with Telegram.
- **Input Layer (User Interface)**: Receives and processes Telegram messages.
- **Domain Layer**: Contains system entities and business rules.

This separation simplifies maintenance and allows for easy replacement of the flight API if needed.


## Project Requirements 
- **Java 22** installed
- **Maven** (recommended version: 3.8+)
- **Telegram API key** (obtained from BotFather)
- **Amadeus API key** (free or paid version)  

### External API
The project currently uses the **Amadeus API**, which provides data on flights and airports.

I chose this API because it offers a free tier with limited queries. While it has some restrictions, integrating with more comprehensive APIs in the future would allow for **greater scalability**. This means that, over time, the bot could expand beyond flight searches to include **other transportation options, accommodations, activities, and more**.

If you want to switch to a different API, you only need to modify the implementation in the **Infrastructure layer**, keeping the core business logic intact.