import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 500 },
        { duration: '60s', target: 1000 },
        { duration: '20s', target: 0 }
    ]
};

export default function () {

    http.get(
        'http://localhost:8080/api/benchmark/virtual/io'
    );

    sleep(1);
}