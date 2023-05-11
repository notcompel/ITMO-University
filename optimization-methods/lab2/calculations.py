import os
import psutil
import time


def measure_memory_usage(func):
    """Decorator function to measure memory usage of a function"""

    def wrapper(*args, **kwargs):
        # Start process memory usage
        process = psutil.Process(os.getpid())
        start_memory = process.memory_info().rss / 1024.0 / 1024.0
        # Call the function and get the result
        result = func(*args, **kwargs)
        # End process memory usage
        end_memory = process.memory_info().rss / 1024.0 / 1024.0
        # Calculate memory usage difference
        mem_diff = end_memory - start_memory
        print(f"{mem_diff:.2f}")
        return result

    return wrapper


def measure_time(func):
    def wrapper(*args, **kwargs):
        start_time = time.time()
        result = func(*args, **kwargs)
        end_time = time.time()
        print(f"{end_time - start_time:.5f}")
        return result

    return wrapper
