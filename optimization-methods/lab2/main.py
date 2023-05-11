import math

import numpy as np
from numpy import cos, sin

from descent import GradientDescent
from linear_regression import test_linear_regression, generate_functions_and_grads
from upgrades import UpgradeType


def const_lr(step):
    return 0.01


def big_const_lr(step):
    return 0.2


def exp_lr(step):
    return 0.1 * math.exp(-0.5 * step - 2)


def f(x):
     # return 10 * x[0] ** 2 + x[1] ** 2
    return x[0] ** 4 + x[1] ** 4 - 4 * x[0] ** 2 - 5 * x[1] ** 2 - x[0] - x[1]


def grad_f(x):
     # return np.array([20 * x[0], 2 * x[1]])
    return np.array([4 * x[0] ** 3 - 8 * x[0] - 1, 4 * x[1] ** 3 - 10 * x[1] - 1])


# Testing
points, f_set, grad_set = generate_functions_and_grads(50)


# for i in range(50):
#     print("BATCH_SIZE", i + 1)
#     test_linear_regression(50, i + 1, const_lr, UpgradeType.Empty, points, f_set, grad_set)

# test_linear_regression(50, 2, const_lr, UpgradeType.Empty, points, f_set, grad_set)
# test_linear_regression(50, 15, const_lr, UpgradeType.Empty, points, f_set, grad_set)
# test_linear_regression(50, 50, const_lr, UpgradeType.Empty, points, f_set, grad_set)

# print("empty")
# test_linear_regression(50, 20, const_lr, UpgradeType.Empty, points, f_set, grad_set)
# print("momentum")
# test_linear_regression(50, 20, const_lr, UpgradeType.Momentum, points, f_set, grad_set)
# print("nesterov")
# test_linear_regression(50, 20, const_lr, UpgradeType.Nesterov, points, f_set, grad_set)
# print("adagrad")
# test_linear_regression(50, 20, big_const_lr, UpgradeType.AdaGrad, points, f_set, grad_set)
# print("rmsprop")
# test_linear_regression(50, 20, big_const_lr, UpgradeType.RMSProp, points, f_set, grad_set)
# print("adam")
# test_linear_regression(50, 20, big_const_lr, UpgradeType.Adam, points, f_set, grad_set)

test_f = f
test_grad = grad_f

descent = GradientDescent(
    test_f, test_grad, np.array([-3.0, 2.0]), const_lr
)


descent.process(UpgradeType.Empty)
descent.process(UpgradeType.Nesterov)
descent.process(UpgradeType.Momentum)
descent.process(UpgradeType.AdaGrad)
descent.process(UpgradeType.RMSProp)
descent.process(UpgradeType.Adam)
