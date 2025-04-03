# GroceryStoreApp 🛒

A modern JavaFX-based grocery store management system that provides separate interfaces for customers, carriers, and store owners.

## Features 🌟

### For Customers 👥
- Browse and search products by category
- Add items to shopping cart
- View order history and track deliveries
- Modern, user-friendly interface
- Real-time order status updates

### For Carriers 🚚
- View and manage active deliveries
- Update delivery status in real-time
- Access delivery history
- Track performance metrics
- Route optimization suggestions

### For Store Owners 💼
- Comprehensive dashboard with business metrics
- Inventory management
- Order processing and tracking
- Customer management
- Sales analytics and reporting
- Store settings configuration
- Database backup and restore

## Technology Stack 💻

- Java 21
- JavaFX for the user interface
- SQLite for data persistence
- Maven for dependency management
- CSS for styling

## Prerequisites 📋

- Java Development Kit (JDK) 21 or later
- Maven 3.6 or later
- Git

## Installation 🔧

1. Clone the repository:
```bash
git clone https://github.com/samettok0/GroceryStoreApp.git
cd GroceryStoreApp
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn javafx:run
```

## Project Structure 📁

```
src/
├── main/
│   ├── java/com/example/ecom/
│   │   ├── controller/    # JavaFX controllers
│   │   ├── model/         # Data models
│   │   ├── service/       # Business logic
│   │   ├── util/          # Utility classes
│   │   └── Main.java      # Application entry point
│   └── resources/
│       ├── com/example/ecom/
│       │   ├── styles.css # Application styles
│       │   └── *.fxml     # JavaFX layout files
│       └── img/           # Application images
└── test/                  # Test files
```

## Configuration ⚙️

1. Database Configuration:
   - Create a `database.properties` file in the project root
   - Add your database connection details:
     ```properties
     db.url=jdbc:sqlite:grocery_store.db
     db.user=your_username
     db.password=your_password
     ```

2. Application Settings:
   - Modify `application.properties` for custom settings:
     ```properties
     store.name=Your Store Name
     store.address=Store Address
     store.phone=Contact Number
     store.email=contact@email.com
     ```

## Contributing 🤝

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments 🙏

- Icons provided by [Material Design Icons](https://materialdesignicons.com/)
- UI design inspired by modern Material Design principles
- Special thanks to all contributors

## Screenshots 📸

### Customer Dashboard
![Customer Dashboard](screenshots/customer_dashboard.png)

### Carrier Dashboard
![Carrier Dashboard](screenshots/carrier_dashboard.png)

### Owner Dashboard
![Owner Dashboard](screenshots/owner_dashboard.png)

## Support 💬

For support, please open an issue in the GitHub repository or contact the development team at support@example.com.

## Roadmap 🗺️

- [ ] Implement real-time notifications
- [ ] Add support for multiple stores
- [ ] Integrate payment gateway
- [ ] Mobile app development
- [ ] Advanced analytics and reporting
- [ ] Customer loyalty program 