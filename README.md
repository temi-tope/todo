# To-Do List API

This project is a RESTful API for a To-Do List application built with Spring Boot. It allows users to manage their tasks by creating, viewing, updating, and deleting to-do items.

## Features

- **Create To-Do Items**: Add new tasks with attributes like title, description, due date, and priority.
- **Read To-Do Items**: Retrieve all tasks with optional sorting and filtering by priority or due date.
- **Update To-Do Items**: Modify task details such as title, description, due date, or mark as completed.
- **Delete To-Do Items**: Remove tasks from the list with support for soft deletion.

## Technology Stack

- **Java**: The primary language for development.
- **Spring Boot**: Framework for building the REST API.
- **Spring Data JPA**: Simplifies database access and operations.
- **Database**: H2 for development and MySQL for production.
- **AWS**: Cloud platform for deployment.

## Setup and Installation

### Prerequisites

- *Java 17
- *Maven* 
- *MySQL* (optional, for production database)

### Clone the Repository

```bash
git clone https://github.com/temi-tope/todo.git
cd todo
