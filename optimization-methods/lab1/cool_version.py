# -*- coding: utf-8 -*-
"""Cool version.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1El1T70nABN7EuNi-giQljqvz7-cTZ75c
"""

import numpy as np
import matplotlib.pyplot as plt
import math


def generate(n, k):
    A = np.random.rand(n, n)
    A = A.T @ A
    U, S, Vt = np.linalg.svd(A)
    S = np.linspace(1, k, n)
    S = np.diag(S)
    A = U @ S @ Vt

    b = np.random.rand(n)
    c = np.random.rand()

    def f(x):
        return 0.5 * x.T @ A @ x + b.T @ x + c

    def grad_f(x):
        return A @ x + b

    return f, grad_f


def visualize(f, hist):
    # Define the range of values for x and y
    x_vals = np.linspace(-5, 5, 1000)
    y_vals = np.linspace(-5, 5, 1000)

    # Create a 2D meshgrid
    X, Y = np.meshgrid(x_vals, y_vals)

    # Compute the function values over the meshgrid
    Z = f([X, Y])

    # Plot the level lines of the function
    plt.contour(X, Y, Z, levels=np.logspace(-1, 3, 10))

    # Plot the path taken by gradient descent
    plt.plot(*zip(*hist), 'o-', color='r')

    # Show the plot
    plt.show()

# Returns [step, count_f, count_grad]
def const_lr(vector, f: callable, grad_f: callable) -> tuple[float, int, int]:
    return 0.01, 0, 0


def generate_lr(lr):
  return lambda vector, f, grad_f: tuple([lr, 0, 0])


# Returns [step, count_f, count_grad]
def dichotomy(vector, f: callable, grad_f: callable) -> tuple[float, int, int]:
    count_f = 0

    eps = 0.001
    left = 0.0005
    right = 0.05

    grad = grad_f(vector)

    while right - left >= eps:
        mid = (left + right) / 2
        m1 = mid + eps
        m2 = mid - eps

        vector1 = vector - m1 * grad
        vector2 = vector - m2 * grad

        f1 = f(vector1)
        f2 = f(vector2)

        if f1 >= f2:
            right = mid
        else:
            left = mid
        count_f += 2

    return right, count_f, 1


def generate_wolfe(start_alpha, c1, c2, max_iter):

    def wolfe(vector, f: callable, grad_f: callable) -> tuple[float, int, int]:
        alpha = start_alpha

        f_val = f(vector)
        grad_val = grad_f(vector)

        count_f = 1
        count_grad = 1

        for i in range(max_iter):
            count_f += 1

            v_new = vector - alpha * grad_val
            f_val_new = f(v_new)

            if f_val_new <= f_val + c1 * alpha * np.dot(grad_val, grad_val):  # Armijo condition
                count_grad += 1
                if np.abs(np.dot(grad_f(v_new), grad_val)) <= c2 * np.dot(grad_val, grad_val):  # Curvature condition
                    break

            alpha /= 2

        return alpha, count_f, count_grad

    return wolfe

# Returns [hist, count_f, count_grad]
def calculate_history(f, grad_f, start_pos, learning_rate, print_steps) -> tuple[np.array, int, int]:
    count_grad, count_f = 0, 0

    vector = start_pos
    hist = [tuple(vector)]

    eps = 0.001
    it = 0

    while it < 10000 and (len(hist) <= 2 or any(abs(hist[-2][i] - hist[-1][i]) >= eps for i in range(len(vector)))):
        grad_v = grad_f(vector)
        count_grad += 1

        step, df, dgrad = learning_rate(vector, f, grad_f)
        count_f += df
        count_grad += dgrad

        vector -= step * grad_v

        hist.append(tuple(vector))
        if print_steps and it % 10 == 0:
            print(f"{it}: {' '.join(f'%.8f' % abs(hist[-2][i] - hist[-1][i]) for i in range(len(vector)))}")

        it += 1

    return np.array(hist), count_f, count_grad


# Returns [count_f, count_grad]
def do_gradient_decent(
        f: callable,
        grad_f: callable,
        start_pos: np.array,
        learning_rate: callable,
        show_plot=False,
        print_steps=False
) -> tuple[int, int]:
    hist, count_f, count_grad = calculate_history(f, grad_f, start_pos, learning_rate, print_steps)

    if show_plot:
        visualize(f, hist)

    return count_f, count_grad

def manual_f(vector):
    return 10 * vector[0] ** 2 + vector[1] ** 2


def manual_grad_f(vector):
    return np.array([10 * 2 * vector[0], 2 * vector[1]])


def normalized_f(s):
    return lambda vector: manual_f([vector[0] / s, vector[1]])


def normalized_grad(s):
    return lambda vector: manual_grad_f([vector[0] / s, vector[1]])

def test_start_dependency(x, y):  # x, y are the analytical minimum
    start = -10
    end = 10
    step = 1

    for dx in range(start, end + 1, step):
        for dy in range(start, end + 1, step):
            count_f, count_grad = do_gradient_decent(
                f=manual_f,
                grad_f=manual_grad_f,
                start_pos=[x + dx, y + dy],
                learning_rate=dichotomy
            )
            print("x: {} \ty: {} \tf: {} \tgrad: {}".format(x + dx, y + dy, count_f, count_grad))


def test_normalization_dependency():
    for i in range(10, 100, 1000):
        count_f, count_grad = do_gradient_decent(
            f=normalized_f(i),
            grad_f=normalized_grad(i),
            start_pos=[-5, -5],
            learning_rate=dichotomy
        )

        print("Normalization: {} \tf: {} \tgrad_f: {}".format(i, count_f, count_grad))


def test_lr_dependency():
  lr = 1
  while (lr <= 1000):
    count_f, count_grad = do_gradient_decent(
        f=manual_f,
        grad_f=manual_grad_f,
        start_pos=[-5, 5],
        learning_rate=generate_lr(lr * 0.001)
    )
    print("Lr:", round(lr * 0.001, 3), "grad:", count_grad)
    lr += 1
    if (lr > 10):
      lr += 9


def test_generated_function(n, k, max_iter):
    count_f, count_grad = 0, 0
    for i in range(max_iter):
        f, grad_f = generate(n, k)
        df, dgrad = do_gradient_decent(
            f=f,
            grad_f=grad_f,
            start_pos=np.random.rand(n),
            learning_rate=const_lr
        )
        count_f += df
        count_grad += dgrad
    return count_f / max_iter, count_grad / max_iter


def test_n_dependency():
    for k in [5, 10, 15]:
      with open("n_stat/dichotomy_k{}.txt".format(k), "w") as n_stat:
          for n in range(1, 41):
              print(n)
              count_f, count_grad = test_generated_function(n=n * 25, k=k, max_iter=10)
              n_stat.write("n: {} f: {} grad {}\n".format(n * 25, count_f, count_grad))


def test_k_dependency():
    for n in [5, 10, 15]:
      with open("k_stat/dichotomy_n{}.txt".format(n), "w") as k_stat:
          for k in range(1, 41):
              print(k)
              count_f, count_grad = test_generated_function(n=n, k=k * 25, max_iter=10)
              k_stat.write("k: {} f: {} grad {}\n".format(k * 25, count_f, count_grad))

def main():
  test_lr_dependency()

  # test_n_dependency()
  # test_k_dependency()
  # count_f, count_grad = do_gradient_decent(
  #     f=manual_f,
  #     grad_f=manual_grad_f,
  #     start_pos=[-5, -5],
  #     learning_rate=const_lr,
  #     show_plot=True,
  #     print_steps=True
  # )
  
  # print("f: {} grad: {}".format(count_f, count_grad))


main()