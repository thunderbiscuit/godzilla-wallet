# Readme

Godzilla Wallet is a bitcoin desktop wallet application using [bdk-jvm](https://central.sonatype.com/artifact/org.bitcoindevkit/bdk-jvm) for bitcoin-related operations.

There are currently 2 variants of the application, each maintained on a separate branch:
- [`variant/mvp`](https://github.com/thunderbiscuit/godzilla-wallet/tree/variant/mvp) — Simple template showcasing the dev setup, dependencies, and workflow
- [`variant/kyoto`](https://github.com/thunderbiscuit/godzilla-wallet/tree/variant/point-of-sale) — Point of sale application with Compact Block Filter client on regtest

## Usage

1. Ensure you have a local regtest network live, with peer available at `127.0.0.1:18444` (currently hardcoded).
2. Run the app using `./gradlew run` or `just run`.
3. Alternatively, you can build the release and install it as a standalone application using one of:
```shell
./gradlew packageDmg # macOS
./gradlew packageDeb # Linux
./gradlew packageMsi # Windows
``` 

## Quick info

- The Material 3 components from Google are readily available in Compose for Desktop, but they look and feel a bit too much like a stock Android app IMO.
- You can find custom compose components at [composables.com](https://composables.com/) and [compose unstyled](https://composeunstyled.com/).
- This application uses [Lucide Icons](https://lucide.dev/), which are made available for compose directly through [compose icons](https://composeicons.com/icon-libraries/lucide).
- The application uses the MVI pattern for domain-level state management.
- The application writes data to the `~/.godzilla/` directory.
