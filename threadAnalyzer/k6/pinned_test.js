import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 10 },
        { duration: '90s', target: 40 },
        { duration: '10s', target: 0 }
    ]
};

export default function () {

    http.get(
        'http://localhost:8080/api/benchmark/virtual/pinning',{
            timeout:'120s'
        }
    );
}