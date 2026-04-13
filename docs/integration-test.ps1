Write-Host "=== Emergency System Integration Tests ===" -ForegroundColor Cyan
$passed = 0
$failed = 0

function Test-Endpoint {
    param($name, $expected, $actual)
    if ($actual -eq $expected) {
        Write-Host "  PASS: $name" -ForegroundColor Green
        $script:passed++
    } else {
        Write-Host "  FAIL: $name (expected $expected got $actual)" -ForegroundColor Red
        $script:failed++
    }
}

Write-Host "`n[1] Auth Tests" -ForegroundColor Yellow
try {
    $reg = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -ContentType "application/json" -Body ('{"fullName":"Integration Test","email":"inttest' + (Get-Date -Format 'HHmmss') + '@test.com","password":"test1234"}')
    Test-Endpoint "Register returns id" $true ($null -ne $reg.id)
} catch { $script:failed++; Write-Host "  FAIL: Register" -ForegroundColor Red }

try {
    $login = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body '{"email":"gateway@test.com","password":"test1234"}'
    $token = $login.accessToken
    $userId = $login.userId
    Test-Endpoint "Login returns token" $true ($null -ne $token)
} catch { $script:failed++; Write-Host "  FAIL: Login" -ForegroundColor Red }

Write-Host "`n[2] Gateway JWT Tests" -ForegroundColor Yellow
try {
    $me = Invoke-RestMethod -Uri "http://localhost:8080/api/users/me" -Method GET -Headers @{Authorization="Bearer $token"}
    Test-Endpoint "GET /me with token" $true ($null -ne $me.userId)
} catch { $script:failed++; Write-Host "  FAIL: GET /me with token" -ForegroundColor Red }

try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/users/me" -Method GET
    $script:failed++; Write-Host "  FAIL: should have been 401" -ForegroundColor Red
} catch {
    Test-Endpoint "GET /me without token = 401" "Unauthorized" $_.Exception.Response.StatusCode
}

Write-Host "`n[3] Incident Tests" -ForegroundColor Yellow
try {
    $h = @{"Content-Type"="application/json"; "Authorization"="Bearer $token"; "X-User-Id"=$userId}
    $inc = Invoke-RestMethod -Uri "http://localhost:8080/api/incidents" -Method POST -Headers $h -Body '{"type":"MEDICAL","severity":3,"description":"Integration test","latitude":19.076,"longitude":72.877,"address":"Test","city":"mumbai"}'
    $incidentId = $inc.id
    Test-Endpoint "Create incident status=REPORTED" "REPORTED" $inc.status
} catch { $script:failed++; Write-Host "  FAIL: Create incident" -ForegroundColor Red }

try {
    $h = @{"Content-Type"="application/json"; "Authorization"="Bearer $token"; "X-User-Id"=$userId}
    Invoke-RestMethod -Uri "http://localhost:8080/api/incidents/$incidentId/status" -Method PATCH -Headers $h -Body '{"status":"QUEUED"}' | Out-Null
    Invoke-RestMethod -Uri "http://localhost:8080/api/incidents/$incidentId/status" -Method PATCH -Headers $h -Body '{"status":"DISPATCHED"}' | Out-Null
    $r = Invoke-RestMethod -Uri "http://localhost:8080/api/incidents/$incidentId/status" -Method PATCH -Headers $h -Body '{"status":"IN_PROGRESS"}'
    Test-Endpoint "State machine transitions" "IN_PROGRESS" $r.status
} catch { $script:failed++; Write-Host "  FAIL: State machine" -ForegroundColor Red }

try {
    $h = @{"Content-Type"="application/json"; "Authorization"="Bearer $token"; "X-User-Id"=$userId}
    Invoke-RestMethod -Uri "http://localhost:8080/api/incidents/$incidentId/status" -Method PATCH -Headers $h -Body '{"status":"REPORTED"}'
    $script:failed++; Write-Host "  FAIL: should have been 409" -ForegroundColor Red
} catch {
    Test-Endpoint "Invalid transition = 409" "Conflict" $_.Exception.Response.StatusCode
}

Write-Host "`n=== Results ===" -ForegroundColor Cyan
Write-Host "Passed: $passed" -ForegroundColor Green
Write-Host "Failed: $failed" -ForegroundColor Red
Write-Host "Total:  $($passed + $failed)"
