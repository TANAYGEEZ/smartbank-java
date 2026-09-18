# Use Case Diagram

## System Use Cases

```mermaid
flowchart LR
    User(["Bank User"])
    
    subgraph SmartBank["SmartBank System"]
        UC1["Create Account"]
        UC2["View Account"]
        UC3["Update Account"]
        UC4["Delete Account"]
        UC5["List All Accounts"]
        UC6["Deposit Money"]
        UC7["Withdraw Money"]
        UC8["Transfer Money"]
        UC9["Check Balance"]
        UC10["View Transaction History"]
        UC11["Generate Account Summary"]
    end
    
    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    User --> UC9
    User --> UC10
    User --> UC11
    
    subgraph Storage["File Storage"]
        S1["accounts.dat"]
        S2["transactions.dat"]
    end
    
    UC1 --> S1
    UC3 --> S1
    UC4 --> S1
    UC6 --> S1
    UC6 --> S2
    UC7 --> S1
    UC7 --> S2
    UC8 --> S1
    UC8 --> S2
```

## Use Case Descriptions

| Use Case | Actor | Description | Precondition | Postcondition |
|---|---|---|---|---|
| Create Account | User | Creates a new Savings or Current account | Valid input data | Account saved to file |
| View Account | User | Displays detailed account information | Account exists | Account details shown |
| Update Account | User | Modifies account holder details | Account exists | Updated data saved |
| Delete Account | User | Removes an account | Account exists, user confirms | Account removed from file |
| List All Accounts | User | Shows all accounts in table format | None | All accounts displayed |
| Deposit Money | User | Adds money to an account | Account exists, amount > 0 | Balance increased, transaction recorded |
| Withdraw Money | User | Removes money from an account | Account exists, sufficient balance | Balance decreased, transaction recorded |
| Transfer Money | User | Moves money between accounts | Both accounts exist, sufficient balance | Balances updated, transactions recorded |
| Check Balance | User | Shows current account balance | Account exists | Balance displayed |
| View History | User | Shows transaction history | Account exists | Transactions listed |
| Account Summary | User | Generates comprehensive report | Account exists | Summary report displayed |
