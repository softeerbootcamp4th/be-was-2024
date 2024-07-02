from locust import HttpUser, task, between, TaskSet

class UserBehavior(TaskSet):

    @task(10)
    def indexTest(self):
        self.client.get("/index.html")


class WebsiteUser(HttpUser):
    tasks = [UserBehavior]
    wait_time = between(1, 2)
    host = "http://localhost:8080"  # Replace with your Spring Boot application's host