class Item
  def initialize(d, t, r, c)
    @dir = d
    @title = t
    @rating = r
    @category = c
  end
  def dir  
    @dir  
  end  
  def title  
    @title  
  end  
  def rating  
    @rating
  end  
  def to_s
      "#{@rating}\t#{@category}\t#{@dir}\t#{@title}"
  end 
end