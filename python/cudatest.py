import time

import numpy as np
from numba import cuda

blocks_per_grid = 10
threads_per_block = 512
no_of_loop = 20000


@cuda.jit
def gpu_sum_kernel(x, result, out):
    idx = cuda.grid(1)
    out[idx] = x[idx] + result[idx]


def batch_add(d_a, d_b):
    # create output data on the device
    # we decide to use 32 blocks, each containing 128 threads
    gpu_sum_kernel[blocks_per_grid, threads_per_block](d_a, d_b, d_out)
    # wait for all threads to complete
    cuda.synchronize()
    # copy the output array back to the host system
    # and print it
    return d_out


if __name__ == '__main__':
    size = blocks_per_grid * threads_per_block * no_of_loop
    a = np.ones(blocks_per_grid * threads_per_block)
    b = np.zeros(blocks_per_grid * threads_per_block)
    start_time = time.time_ns()
    print("Sample Size")
    print(size)
    print("Batch Size")
    print(blocks_per_grid * threads_per_block)

    print("cuda")
    d_a = cuda.to_device(a)
    d_b = cuda.to_device(b)
    d_out = cuda.device_array_like(d_a)
    for x in range(no_of_loop):
        d_b = batch_add(d_a, d_b)
    # print((time.time_ns() - start_time)/1000000)
    # print(np.sum(d_b.copy_to_host()))
    print((time.time_ns() - start_time) / 1000000)

    print("Numpy sum")
    start_time = time.time_ns()
    a = np.ones(size)
    # print(np.sum(a))
    print((time.time_ns() - start_time) / 1000000)

    print("normal sum")
    start_time = time.time_ns()
    a = np.ones(blocks_per_grid * threads_per_block * no_of_loop)
    sum = 0

    for i in range(size):
        sum = sum + a[i]
    # print (sum)
    print((time.time_ns() - start_time) / 1000000)
