function change(amount)
  if math.type(amount) ~= "integer" then
    error("Amount must be an integer")
  end
  if amount < 0 then
    error("Amount cannot be negative")
  end
  local counts, remaining = {}, amount
  for _, denomination in ipairs({25, 10, 5, 1}) do
    counts[denomination] = remaining // denomination
    remaining = remaining % denomination
  end
  return counts
end


function first_then_lower_case(array, predicate)
  for _, value in ipairs(array) do
    if predicate(value) then
      return value:lower()
    end
  end
end


function powers_generator(base, limit)
  local power = 1
  return coroutine.create(function()
    while power <= limit do
      coroutine.yield(power)
      power = power * base
    end
  end)
end


function say(word)
  if word == nil then
    return ""
  end
  return function(next)
    if next == nil then
      return word
    else
      return say(word .. " " .. next)
    end
  end
end


function meaningful_line_count(filename)
  local file, err = io.open(filename, "r")
  if not file then
    error("No such file: " .. filename)
  end

  local count = 0
  for line in file:lines() do
    local trimmed_line = line:match("^%s*(.-)%s*$")
    if #trimmed_line > 0 and not trimmed_line:match("^#") then
      count = count + 1
    end
  end

  file:close()
  return count
end


Quaternion = (function()
  local Quaternion = {}
  Quaternion.__index = Quaternion

  function Quaternion.new(a, b, c, d)
    return setmetatable({a = a, b = b, c = c, d = d}, Quaternion)
  end

  function Quaternion:__add(q)
    if getmetatable(q) ~= getmetatable(self) then
      error("Attempt to add a non-Quaternion value")
    end
    return Quaternion.new(self.a + q.a, self.b + q.b, self.c + q.c, self.d + q.d)
  end

  function Quaternion:__mul(q)
    if getmetatable(q) ~= getmetatable(self) then
      error("Attempt to multiply by a non-Quaternion value")
    end
    local a1, b1, c1, d1 = self.a, self.b, self.c, self.d
    local a2, b2, c2, d2 = q.a, q.b, q.c, q.d
    return Quaternion.new(
      a1*a2 - b1*b2 - c1*c2 - d1*d2,
      a1*b2 + b1*a2 + c1*d2 - d1*c2,
      a1*c2 - b1*d2 + c1*a2 + d1*b2,
      a1*d2 + b1*c2 - c1*b2 + d1*a2
    )
  end

  function Quaternion:__eq(q)
    if getmetatable(q) ~= getmetatable(self) then
      return false
    end
    return self.a == q.a and self.b == q.b and self.c == q.c and self.d == q.d
  end

  function Quaternion:__tostring()
    local parts = {}
    if self.a ~= 0 then table.insert(parts, string.format("%.1f", self.a)) end
    if self.b ~= 0 then table.insert(parts, (self.b > 0 and "+" or "") .. string.format("%.1f", self.b) .. "i") end
    if self.c ~= 0 then table.insert(parts, (self.c > 0 and "+" or "") .. string.format("%.1f", self.c) .. "j") end
    if self.d ~= 0 then table.insert(parts, (self.d > 0 and "+" or "") .. string.format("%.1f", self.d) .. "k") end
    return table.concat(parts) == "" and "0" or table.concat(parts)
  end

  function Quaternion:coefficients()
    return {self.a, self.b, self.c, self.d}
  end

  function Quaternion:conjugate()
    return Quaternion.new(self.a, -self.b, -self.c, -self.d)
  end

  return setmetatable(Quaternion, {__index = Quaternion})
end)()
